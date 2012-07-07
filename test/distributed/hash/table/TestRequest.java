package distributed.hash.table;

import static org.junit.Assert.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestRequest {
    private final int mServerCount = 4;
    public final int[] mPortMap = {15555,15556,15557,15558};
    private IDistributedHashTable[] mDhtClientArray = null;
    private int mRequestId = 1;
    // private int mKeyId = 1;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
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
            IRequest req = new Request(1, mRequestId++, 1, 1);
            mDhtClientArray[0].insert(req);
            System.out.println("DHTClient insert: " + req.toString());

            req = new Request(1, mRequestId++, 2, 2);
            mDhtClientArray[0].insert(req);
            System.out.println("DHTClient insert: " + req.toString());

            req = new Request(1, mRequestId++, 3, 3);
            mDhtClientArray[0].insert(req);
            System.out.println("DHTClient insert: " + req.toString());

            req = new Request(1, mRequestId++, 2, 0);
            Object value = mDhtClientArray[0].lookup(req);
            System.out.println("DHTClient lookup: " + value.toString());

            System.out.println("DHTClient get Count on machine id 1 is " + mDhtClientArray[0].count());
            
            Integer intValue = new Integer(value.toString());
            
            assertTrue(intValue.intValue() == 2);
        }  catch(Exception e) {
            System.out.println("dhtClient: " +  e.getMessage());
        }
    }
}