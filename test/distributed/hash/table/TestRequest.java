package distributed.hash.table;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestRequest {
    private int mServerCount;
    private int[] mPortMap;
    private IDistributedHashTable[] mDhtClientArray = null;
    private int mRequestId = 1;
    // private int mKeyId = 1;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
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
    public void testExperiment1() {
        try {
            IInsertRequest iReq = new InsertRequest(mRequestId++, 1, 1, 1);
            mDhtClientArray[0].insert(iReq);
            System.out.println("DHTClient insert: " + iReq.getMessage());

            iReq = new InsertRequest(mRequestId++, 3, 2, 2);
            mDhtClientArray[2].insert(iReq);
            System.out.println("DHTClient insert: " + iReq.getMessage());

            iReq = new InsertRequest(mRequestId++, 4, 982345, 756321);
            mDhtClientArray[3].insert(iReq);
            System.out.println("DHTClient insert: " + iReq.getMessage());

            IQueryRequest qReq = new QueryRequest(mRequestId++, 4, 2);
            Object value = mDhtClientArray[3].lookup(qReq);
            System.out.println("DHTClient lookup: " + value.toString());

            System.out.println("DHTClient get Count on machine id 1 is " + mDhtClientArray[0].count());
            
            Integer intValue = new Integer(value.toString());
            
            assertTrue(intValue.intValue() == 2);
        }  catch(Exception e) {
            System.out.println("dhtClient: " +  e.getMessage());
        }
    }
}