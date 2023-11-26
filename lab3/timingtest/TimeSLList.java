package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(SLList<Integer> Ns, SLList<Double> times, SLList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }
    private static void loopAdd(SLList A, int N) {
        for (int i = 0; i < N; i++) {
            A.addLast(i);
        }
    }

    public static void timeGetLast() {
        SLList Ns = new SLList<Integer>();
        SLList times = new SLList<Double>();
        SLList opCounts = new SLList<Integer>();
        for (int N = 1000; N <= 128000; N = 2 * N) {
            SLList A = new SLList();
            loopAdd(A, N);

            Stopwatch sw = new Stopwatch();
            int M = 0;
            for (; M < 1000; M++) {
                A.getLast();
            }
            double timeInSeconds = sw.elapsedTime();

            Ns.addLast(N);
            times.addLast(timeInSeconds);
            opCounts.addLast(M);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
