package distributed.hash.table;

import static org.junit.Assert.*;

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

public class TestExperiment1 {
    private int mServerCount;
    private int[] mPortMap;
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
    public void testExperiement1() {
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
                int machineClientId = mRandom.nextInt(mServerCount);
                int machineId = machineClientId + 1;
                int key = mRandom.nextInt(1000000) + 1;
                IQueryRequest req = new QueryRequest(mRequestId++, machineId, key);

                mStopwatch.start(); 
                mDhtClientArray[machineClientId].lookup(req);
                mStopwatch.stop();
                System.out.println("DHTServer[" + machineId + "] lookup on empty took " + mStopwatch.getElapsedTime());

                assertTrue(0 == mDhtClientArray[machineClientId].count());
            }  catch(Exception e) {
                e.printStackTrace();
                System.out.println("dhtClient: " +  e.getMessage());
            }
        }
    }
}