package com.echo_system.game.world;

import java.util.Arrays;

public class LBM_solver {

	static final double four9ths = 4.0 / 9.0;
	static final double one9ths = 1.0 / 9.0;
	static final double one36ths = 1.0 / 36.0;

	public enum LATTICE {
		twoD_8Direction(new double[]{four9ths, one9ths, one9ths, one9ths, one9ths, one36ths, one36ths, one36ths, one36ths},
				new double[][]{{0.0, 0.0}, {1.0, 0.0}, {-1.0, 0.0}, {0.0, 1.0}, {0.0, -1.0}, {1.0, 1.0}, {1.0, -1.0}, {-1.0, 1.0}, {-1.0, -1.0}},
				new int[]{0, 2, 1, 4, 3, 8, 7, 6, 5}
		);

		double[] wi;
		double[][] ei;
		int[][] iei;
		private int[] reverse;

		LATTICE(double[] wi, double[][] ei, int[] reverseIn) {
			this.ei = ei;
			this.wi = wi;
			this.iei = new int[ei.length][ei[0].length];
			for (int i = 0; i < ei.length; i++) {
				for (int i1 = 0; i1 < ei[i].length; i1++) {
					iei[i][i1] = (int) ei[i][i1];
				}
			}
			this.reverse = reverseIn;
		}
	}

	private int height, width;
	private double omega;
	private double[] wi;
	private double[][] ei;
	private int[][] iei;
	private int[] reverse;

	public LBM_solver(int width, int height, double omega, double[] wi, double[][] ei, int[][] iei, int[] reverse) {
		this.height = height;
		this.width = width;
		this.omega = omega;
		this.wi = wi;
		this.ei = ei;
		this.iei = iei;
		this.reverse = reverse;
	}

	public LBM_solver(int width, int height, double omega, LATTICE la) {
		this.height = height;
		this.width = width;
		this.omega = omega;
		this.wi = la.wi;
		this.ei = la.ei;
		this.iei = la.iei;
		this.reverse = la.reverse;
	}

	public double[][] stream_noblock(double[][] f) {
		double[][] result = new double[f.length][0];
		for (int i = 0; i < f.length; i++) {
			result[i] = f[i];
			if (iei[i][0] != 0) {
				result[i] = rotatex(result[i], iei[i][0]);
			}
			if (iei[i][1] != 0) {
				result[i] = rotatey(result[i], iei[i][1]);
			}
		}
		return result;
	}

	public void stream(double[][] ni, boolean[][] block) {
		double[][] ni_new = stream_noblock(ni);
		for (int x = 0; x < width * height; x++) {
			for (int i = 1; i < ni.length; i++) {
				if (block[i][x]) {
					ni_new[i][x] = ni[reverse[i]][x];
				}
			}
		}
		for (int i = 1; i < ni.length; i++) {
			ni[i] = ni_new[i];
		}
	}

	public void update(double[][] ni, double[] rho, double[] ux, double[] uy, int y) {
		rho[y] = 0;
		ux[y] = 0;
		uy[y] = 0;
		for (int i = 0; i < ei.length; i++) {
			rho[y] += ni[i][y];
			ux[y] += ni[i][y] * ei[i][0];
			uy[y] += ni[i][y] * ei[i][1];
		}
		ux[y] /= rho[y];
		uy[y] /= rho[y];
	}

	public void update2(double[][] f, double[][] g, double[] rho_f, double[] rho_g, double[] ux, double[] uy, int y) {
		rho_g[y] = 0;
		rho_f[y] = 0;
		ux[y] = 0;
		uy[y] = 0;
		for (int i = 0; i < ei.length; i++) {
			rho_f[y] += f[i][y];
			rho_g[y] += g[i][y];
			ux[y] += (f[i][y] + g[i][y]) * ei[i][0];
			uy[y] += (f[i][y] + g[i][y]) * ei[i][1];
		}
		ux[y] /= rho_f[y] + rho_g[y];
		uy[y] /= rho_f[y] + rho_g[y];
	}

	public void collide(double[][] ni, double[] rho, double[] ux, double[] uy, int y) {
		double v3 = ux[y] * ux[y] + uy[y] * uy[y];
		for (int i = 0; i < ei.length; i++) {
			double eiv = ux[y] * ei[i][0] + uy[y] * ei[i][1];
			ni[i][y] = (1.0 - omega) * ni[i][y] + omega * wi[i] * rho[y] * (1.0 - 1.5 * v3 + 3.0 * eiv + 4.5 * eiv * eiv);
		}
	}

	public void collide_g(double[][] g, double[] rho, double[] ux, double[] uy, double[] utx, double[] uty, int y) {
		for (int i = 0; i < ei.length; i++) {
			double eiv = (utx[y] + ux[y]) * ei[i][0] + (uty[y] + uy[y]) * ei[i][1];
			g[i][y] = (1.0 - omega) * g[i][y] + omega * wi[i] * rho[y] * (1.0 + 1.5 * eiv);
		}
	}

	public void gravity(double[] t, double[] ux, double[] uy, int y) {
		uy[y] += 0.0001 * (1.0 - t[y]);
	}

	public void thermo(double[] t) {
		for (int x = 0; x < width * height; x++) {
			if (x >= (height - 2) * width) {
				t[x] = 2.0;
			}
			if (x < width * 2) {
				t[x] = 0.0;
			}
		}
	}

	private double[] hh = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	private double[] ff = new double[]{-5 / 3, 1 / 3, 1 / 3, 1 / 3, 1 / 3, 1 / 12, 1 / 12, 1 / 12, 1 / 12};
	double kf = 0.1;

	/*public void collide2(double[][] f, double[][] g, double[] rho_f, double[] rho_g, double[] ux, double[] uy, int y) {
		double v3 = ux[y] * ux[y] + uy[y] * uy[y];
		for (int i = 0; i < ei.length; i++) {
			double eiv = ux[y] * ei[i][0] + uy[y] * ei[i][1];
			double feq = hh[i] * rho_f[y] + ff[i] * (p0 - kf * rho_f[y] * lap_phi[y]) + wi[i] * rho_f[y] * (-1.5 * v3 + 3.0 * eiv + 4.5 * eiv * eiv)
					;
			f[i][y] = (1.0 - omega) * f[i][y] + omega * wi[i] * rho_f[y] * (1.0 - 1.5 * v3 + 3.0 * eiv + 4.5 * eiv * eiv);
			g[i][y] = (1.0 - omega) * g[i][y] + omega * wi[i] * rho_g[y] * (1.0 - 1.5 * v3 + 3.0 * eiv + 4.5 * eiv * eiv);
		}
	}*/

	public double[][] move(double[][][] ni, double[][] array, double[][] rho) {
		double[][] result = new double[array.length][array[0].length];
		for (int y = 1; y < height + 1; y++) {
			for (int x = 1; x < width + 1; x++) {
				for (int i = 0; i < ei.length; i++) {
					result[x + iei[i][0]][y + iei[i][1]] += array[x][y] * ni[i][x - 1][y - 1] / rho[x - 1][y - 1];
				}
			}
		}
		for (int x = 0; x < width + 2; x++) {
			result[x][height] += result[x][0];
			result[x][1] += result[x][height + 1];
		}
		for (int y = 0; y < height + 2; y++) {
			result[width][y] += result[0][y];
			result[1][y] += result[width + 1][y];
		}
		return result;
	}

	/*private INDArray get(INDArray array, int x1, int x2, int y1, int y2) {
		return array.get(NDArrayIndex.interval(x1, x2), NDArrayIndex.interval(y1, y2));
	}

	private INDArray getx(INDArray array, int x1, int x2) {
		return array.get(NDArrayIndex.interval(x1, x2), NDArrayIndex.all());
	}

	private INDArray gety(INDArray array, int y1, int y2) {
		return array.get(NDArrayIndex.all(), NDArrayIndex.interval(y1, y2));
	}

	public void roll(INDArray array, int x, int y) {
		if (x != 0) {
			int x0 = mod(-x, width);
			INDArray a = getx(array, 0, x0).dup();
			getx(array, 0, width - x0).assign(getx(array, x0, width).dup());
			getx(array, width - x0, width).assign(a);
		}
		if (y != 0) {
			int y0 = mod(-y, height);
			INDArray a = gety(array, 0, y0).dup();
			gety(array, 0, height - y0).assign(gety(array, y0, height).dup());
			gety(array, height - y0, height).assign(a);
		}
	}*/

	private int mod(int i, int j) {
		return (i % j) < 0 ? (i % j) + 0 + (j < 0 ? -j : j) : (i % j + 0);
	}

	public double[] curl(double[] array1, double[] array2) {
		double[] result = new double[array1.length];
		double[] ra2n = rotatex(array2, -1);
		double[] ra2p = rotatex(array2, 1);
		double[] ra1n = rotatey(array1, -1);
		double[] ra1p = rotatey(array1, 1);
		for (int x = 0; x < array1.length; x++) {
			result[x] = ra2n[x] - ra2p[x] - ra1n[x] + ra1p[x];
		}
		return result;
	}

	private double sum(double[][] array) {
		double result = 0;
		for (int y = 0; y < array[0].length; y++) {
			for (int x = 0; x < array.length; x++) {
				result += array[x][y];
			}
		}
		return result;
	}

	private void fill2(double[][] array, double init) {
		for (int x = 0; x < array.length; x++) {
			Arrays.fill(array[x], init);
		}
	}

	private void fill2(boolean[][] array, boolean init) {
		for (int x = 0; x < array.length; x++) {
			Arrays.fill(array[x], init);
		}
	}

	public void rotate(double[] array, int x, int y) {
		if (x != 0) {
			array = rotatex(array, x);
		}
		if (y != 0) {
			array = rotatey(array, y);
		}
	}

	private double[] rotatex(double[] array, int x) {
		double[] result = new double[width * height];
		int x0 = mod(x, width);
		int x1 = width - x0;
		for (int i = 0; i < height; i++) {
			int offset = i * width;
			System.arraycopy(array, offset, result, offset + x0, x1);
			System.arraycopy(array, offset + x1, result, offset, x0);
		}
		return result;
	}

	private double[] rotatey(double[] array, int y) {
		double[] result = new double[width * height];
		int x0 = mod(y, height) * width;
		int x1 = width * height - x0;
		System.arraycopy(array, 0, result, x0, x1);
		System.arraycopy(array, x1, result, 0, x0);
		return result;
	}


	public boolean[] rotate(boolean[] array, int x, int y) {
		boolean[] result = array;
		if (x != 0) {
			result = rotatex(result, x);
		}
		if (y != 0) {
			result = rotatey(result, y);
		}
		return result;
	}

	private boolean[] rotatex(boolean[] array, int x) {
		boolean[] result = new boolean[width * height];
		int x0 = mod(x, width);
		int x1 = width - x0;
		for (int i = 0; i < height; i++) {
			int offset = i * width;
			System.arraycopy(array, offset, result, offset + x0, x1);
			System.arraycopy(array, offset + x1, result, offset, x0);
		}
		return result;
	}

	private boolean[] rotatey(boolean[] array, int y) {
		boolean[] result = new boolean[width * height];
		int x0 = mod(y, height) * width;
		int x1 = width * height - x0;
		System.arraycopy(array, 0, result, x0, x1);
		System.arraycopy(array, x1, result, 0, x0);
		return result;
	}

}
