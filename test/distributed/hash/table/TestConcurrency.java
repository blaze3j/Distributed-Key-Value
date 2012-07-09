package distributed.hash.table;

import static org.junit.Assert.assertTrue;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Stopwatch.Stopwatch;

class ClientThread extends Thread {
    private IDistributedHashTable mDhtClient = null;
    private int mMinRange = 0;
    private int mMaxRange = 0;
    private int mMachineId = 0;
    private final int[] mPortMap = {15555,15556,15557,15558};
    
    public ClientThread(int machineId, int minRange, int maxRange) throws Exception{
        mDhtClient = (IDistributedHashTable) 
            Naming.lookup("rmi://localhost:" + mPortMap[(machineId - 1)] + "/DistributedHashTable");
        mMinRange = minRange;
        mMaxRange = maxRange;
        mMachineId = machineId;
    }

    public void run() {
        for (int i = mMinRange; i < mMaxRange; i++)
        {
            IInsertRequest req = new InsertRequest(i, mMachineId, i, i);
            try {
                mDhtClient.insert(req);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

public class TestConcurrency {
    private final int mServerCount = 4;
    public final int[] mPortMap = {15555,15556,15557,15558};
    private IDistributedHashTable[] mDhtClientArray = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        new Random();
        new Stopwatch();
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
    public void testCuncurrency1() {
        final int clientThreadCount = 5;
        final int machineId = 1;
        int total = 0;
        int count = 0;

        for (int i = 0; i < mServerCount; i++) {
            try {
                mDhtClientArray[i].purge();
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("dhtClient: " +  e.getMessage());
            }
        }

        ClientThread[] clientThreadArray = new ClientThread[clientThreadCount];
        
        for (int i = 0; i < clientThreadCount; i++) {
            try {
                clientThreadArray[i] = new ClientThread(machineId, i * 1000 + 1, (i + 1) * 1000 + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            clientThreadArray[i].start();
        }
        
        for (int i = 0; i < clientThreadCount; i++) {
            try {
                clientThreadArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        for (int i = 0; i < mServerCount; i++) {
            try {
                count =  mDhtClientArray[i].count();
                total += count;
                System.out.println("DHTServer[" + (i + 1) + "] count " + count);
                
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("dhtClient: " +  e.getMessage());
            }
        }
        
        assertTrue(total == 1000 * clientThreadCount);
    }
}
