package distributed.hash.table;

import static org.junit.Assert.assertTrue;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConcurrencyExperiment2 {
    private final int mServerCount = 4;
    public final int[] mPortMap = {15555,15556,15557,15558};
    private IDistributedHashTable[] mDhtClientArray = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        new Random();
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
    public void testConcurrencyExperiment2() {
        final int clientThreadCount = 5;
        int count = 0;

        for (int i = 0; i < mServerCount; i++) {
            try {
                mDhtClientArray[i].purge();
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("dhtClient: " +  e.getMessage());
            }
        }

        ClientThreadExperiment2[] clientThreadArray = new ClientThreadExperiment2[clientThreadCount];
        
        for (int i = 0; i < clientThreadCount; i++) {
            try {
                clientThreadArray[i] = new ClientThreadExperiment2(i, i * 1000 + 1, (i + 1) * 1000 + 1);
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
                System.out.println("DHTServer[" + (i + 1) + "] count " + count);
                assertTrue(count != 0);
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("dhtClient: " +  e.getMessage());
            }
        }
    }
}
