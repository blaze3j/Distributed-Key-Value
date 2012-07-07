package distributed.hash.table;

import static org.junit.Assert.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Stopwatch.Stopwatch;

public class TestExperiment1 {
    private final int mServerCount = 4;
    public final int[] mPortMap = {15555,15556,15557,15558};
    private IDistributedHashTable[] mDhtClientArray = null;
    private int mRequestId = 1;
    private Random mRandom = null;
    private Stopwatch mStopwatch = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        mRandom = new Random();
        mStopwatch = new Stopwatch();
        mDhtClientArray = new IDistributedHashTable[mServerCount];
        for (int i = 0; i < mServerCount; i++) {
            mDhtClientArray[i] = (IDistributedHashTable) 
            Naming.lookup("rmi://localhost:" + mPortMap[i] + "/DistributedHashTable");
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link distributed.hash.table.Request#Request(int, int, int, java.lang.Object)}.
     */
    @Test
    public void testInsertionLookup() {
        for (int i = 0; i < mServerCount; i++) {
            try {
                mDhtClientArray[i].purge();
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("dhtClient: " +  e.getMessage());
            }
        }
        mStopwatch.reset();

        for (int i = 0; i < 1000; i++)
        {
            try {
                int machineClientId = mRandom.nextInt(4);
                int machineId = machineClientId + 1;
                int key = mRandom.nextInt(1000000) + 1;
                IQueryRequest req = new QueryRequest(mRequestId++, machineId, key);

                mStopwatch.start(); 
                mDhtClientArray[machineClientId].lookup(req);
                mStopwatch.stop();
                System.out.println("DHTClient[" + machineId + "] lookup on empty took " + mStopwatch.getElapsedTime());

                assertTrue(0 == mDhtClientArray[machineClientId].count());
            }  catch(Exception e) {
                e.printStackTrace();
                System.out.println("dhtClient: " +  e.getMessage());
            }
        }
    }
}