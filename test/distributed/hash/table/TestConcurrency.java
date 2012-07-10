package distributed.hash.table;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private int mServerCount;
    private int[] mPortMap;
    
    public ClientThread(int machineId, int minRange, int maxRange) throws Exception{
    	
    	try{
			java.net.URL path = ClassLoader.getSystemResource("serverSetting.txt");	
			FileReader fr = new FileReader (path.getFile());
	        BufferedReader br = new BufferedReader (fr);
	        try {
				String[] portMap = br.readLine().split(",");
				mServerCount = portMap.length;
				mPortMap = new int[mServerCount];
				for(int i = 0; i < mServerCount; i++){
					mPortMap[i] = Integer.parseInt(portMap[i]);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			System.exit(-1);
		}
    	
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

public class TestConcurrency extends TestExperiment{

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    	super.setUp();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    	super.tearDown();
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
