package org.voltdb;

import java.util.*;

import org.voltdb.catalog.*;
import org.voltdb.messaging.FragmentTaskMessage;
import org.voltdb.plannodes.AbstractPlanNode;
import org.voltdb.plannodes.ReceivePlanNode;
import org.voltdb.plannodes.SendPlanNode;

import edu.brown.BaseTestCase;
import edu.brown.catalog.QueryPlanUtil;
import edu.brown.hashing.DefaultHasher;
import edu.brown.plannodes.PlanNodeUtil;
import edu.brown.utils.PartitionEstimator;
import edu.brown.utils.ProjectType;

public class TestBatchPlanner extends BaseTestCase {

    private static final String SINGLESITE_PROCEDURE = "GetAccessData";
    private static final String SINGLESITE_STATEMENT = "GetData";
    
    private static final String MULTISITE_PROCEDURE = "UpdateLocation";
    private static final String MULTISITE_STATEMENT = "update";
    
    private static final Object SINGLESITE_PROCEDURE_ARGS[] = {
        new Long(1), // S_ID
        new Long(1), // SF_TYPE
    };
    private static final Object MULTISITE_PROCEDURE_ARGS[] = {
        new Long(1),        // VLR_LOCATION
        new String("XXX"),  // SUB_NBR
    };
    
    private static final int LOCAL_PARTITION = 1;
    private static final int REMOTE_PARTITION = 0;
    private static final int NUM_PARTITIONS = 10;
    private static final int INITIATOR_ID = -1;
    
    private Procedure catalog_proc;
    private Statement catalog_stmt;
    private SQLStmt batch[];
    private ParameterSet args[];
    
    @Override
    protected void setUp() throws Exception {
        super.setUp(ProjectType.TM1);
        p_estimator = new PartitionEstimator(catalog_db, new DefaultHasher(catalog_db, NUM_PARTITIONS));
    }
    
    private void init(String proc_name, String stmt_name, Object raw_args[]) {
        this.catalog_proc = catalog_db.getProcedures().get(proc_name);
        assertNotNull(this.catalog_proc);
        this.catalog_stmt = this.catalog_proc.getStatements().get(stmt_name);
        assertNotNull(this.catalog_stmt);
        assert(this.catalog_stmt.getHas_multisited());

        // Create a SQLStmt batch
        this.batch = new SQLStmt[] { new SQLStmt(this.catalog_stmt, this.catalog_stmt.getMs_fragments()) };
        this.args = new ParameterSet[] { VoltProcedure.getCleanParams(this.batch[0], raw_args) };
    }
    
//    /**
//     * testGenerateDependencyIds
//     */
//    public void testGenerateDependencyIds() throws Exception {
//        this.init(MULTISITE_PROCEDURE, MULTISITE_STATEMENT, MULTISITE_PROCEDURE_ARGS);
//        List<PlanFragment> catalog_frags = Arrays.asList(this.catalog_stmt.getMs_fragments().values());
//        assertFalse(catalog_frags.isEmpty());
//        // Shuffle the list
//        Collections.shuffle(catalog_frags);
//        
//        List<Set<Integer>> frag_input_ids = new ArrayList<Set<Integer>>();
//        List<Set<Integer>> frag_output_ids = new ArrayList<Set<Integer>>();
//        BatchPlanner.generateDependencyIds(catalog_frags, frag_input_ids, frag_output_ids);
//        assertEquals(catalog_frags.size(), frag_input_ids.size());
//        assertEquals(catalog_frags.size(), frag_output_ids.size());
//        
//        for (int i = 0, cnt = catalog_frags.size(); i < cnt; i++) {
//            PlanFragment catalog_frag = catalog_frags.get(i);
//            Set<Integer> input_ids = frag_input_ids.get(i);
//            Set<Integer> output_ids = frag_output_ids.get(i);
//            assertNotNull(catalog_frag);
//            assertNotNull(input_ids);
//            assertNotNull(output_ids);
//            
//            // Make sure that if this PlanFragment has an input id for each ReceivePlanNode
//            AbstractPlanNode root = QueryPlanUtil.deserializePlanFragment(catalog_frag);
//            Set<ReceivePlanNode> receive_nodes = PlanNodeUtil.getPlanNodes(root, ReceivePlanNode.class);
//            assertEquals(receive_nodes.size(), input_ids.size());
//            
//            // Likewise, make sure we have an output id for each SendPlanNode
//            Set<SendPlanNode> send_nodes = PlanNodeUtil.getPlanNodes(root, SendPlanNode.class);
//            assertEquals(send_nodes.size(), output_ids.size());
//            
////            System.out.println(catalog_frag);
////            System.out.println("  IN:  " + input_ids);
////            System.out.println("  OUT: " + output_ids);
////            System.out.println(PlanNodeUtil.debug(root));
////            System.out.println("------------");
//        } // FOR
//    }
//    
//    /**
//     * testSingleSitedLocalPlan
//     */
//    public void testSingleSitedLocalPlan() throws Exception {
//        this.init(SINGLESITE_PROCEDURE, SINGLESITE_STATEMENT, SINGLESITE_PROCEDURE_ARGS);
//        BatchPlanner batchPlan = new BatchPlanner(batch, this.catalog_proc, p_estimator, INITIATOR_ID);
//        BatchPlanner.BatchPlan plan = batchPlan.plan(this.args, LOCAL_PARTITION);
//        int local_frags = plan.getLocalFragmentCount();
//        int remote_frags = plan.getRemoteFragmentCount();
//        
//        assertTrue(plan.isLocal());
//        assertTrue(plan.isSingleSited());
//        assertEquals(1, local_frags);
//        assertEquals(0, remote_frags);
//    }
//    
//    /**
//     * testSingleSitedLocalPlan2
//     */
//    public void testSingleSitedLocalPlan2() throws Exception {
//        Object params[] = new Object[] {
//            new Long(LOCAL_PARTITION),  // S_ID
//            new Long(0),                // SF_TYPE
//            new Long(0),                // START_TIME
//            new Long(0),                // END_TIME
//        };
//        
//        this.init("GetNewDestination", "GetData", params);
//        BatchPlanner batchPlan = new BatchPlanner(batch, this.catalog_proc, p_estimator, INITIATOR_ID);
//        BatchPlanner.BatchPlan plan = batchPlan.plan(this.args, LOCAL_PARTITION);
//        int local_frags = plan.getLocalFragmentCount();
//        int remote_frags = plan.getRemoteFragmentCount();
//        
//        assertTrue(plan.isLocal());
//        assertTrue(plan.isSingleSited());
//        assertEquals(1, local_frags);
//        assertEquals(0, remote_frags);
//    }
//    
//    /**
//     * testSingleSitedRemotePlan
//     */
//    public void testSingleSitedRemotePlan() throws Exception {
//        this.init(SINGLESITE_PROCEDURE, SINGLESITE_STATEMENT, SINGLESITE_PROCEDURE_ARGS);
//        BatchPlanner batchPlan = new BatchPlanner(batch, this.catalog_proc, p_estimator, INITIATOR_ID);
//        BatchPlanner.BatchPlan plan = batchPlan.plan(this.args, REMOTE_PARTITION);
//        int local_frags = plan.getLocalFragmentCount();
//        int remote_frags = plan.getRemoteFragmentCount();
//        
//        assertFalse(plan.isLocal());
//        assertTrue(plan.isSingleSited());
//        assertEquals(0, local_frags);
//        assertEquals(1, remote_frags);
//    }
//    
//    /**
//     * testMultiSitedLocalPlan
//     */
//    public void testMultiSitedLocalPlan() throws Exception {
//        this.init(MULTISITE_PROCEDURE, MULTISITE_STATEMENT, MULTISITE_PROCEDURE_ARGS);
//        BatchPlanner batchPlan = new BatchPlanner(batch, this.catalog_proc, p_estimator, INITIATOR_ID);
//        BatchPlanner.BatchPlan plan = batchPlan.plan(this.args, LOCAL_PARTITION);
//        int local_frags = plan.getLocalFragmentCount();
//        int remote_frags = plan.getRemoteFragmentCount();
//        
//        assertFalse(plan.isLocal());
//        assertFalse(plan.isSingleSited());
//        assertEquals(1, local_frags);
//        assertEquals(NUM_PARTITIONS, remote_frags);
//    }
//    
//    /**
//     * testMultiSitedRemotePlan
//     */
//    public void testMultiSitedRemotePlan() throws Exception {
//        this.init(MULTISITE_PROCEDURE, MULTISITE_STATEMENT, MULTISITE_PROCEDURE_ARGS);
//        BatchPlanner batchPlan = new BatchPlanner(batch, this.catalog_proc, p_estimator, INITIATOR_ID);
//        BatchPlanner.BatchPlan plan = batchPlan.plan(this.args, REMOTE_PARTITION);
//        int local_frags = plan.getLocalFragmentCount();
//        int remote_frags = plan.getRemoteFragmentCount();
//         
//        assertFalse(plan.isLocal());
//        assertFalse(plan.isSingleSited());
//        assertEquals(1, local_frags);
//        assertEquals(NUM_PARTITIONS, remote_frags);
//    }

    /**
     * testGetFragmentTaskMessages
     */
    public void testGetFragmentTaskMessages() throws Exception {
        this.init(MULTISITE_PROCEDURE, MULTISITE_STATEMENT, MULTISITE_PROCEDURE_ARGS);
        BatchPlanner batchPlan = new BatchPlanner(batch, this.catalog_proc, p_estimator, INITIATOR_ID);
        BatchPlanner.BatchPlan plan = batchPlan.plan(this.args, LOCAL_PARTITION);
        
        List<FragmentTaskMessage> ftasks = plan.getFragmentTaskMessages(0, -1);
//        System.err.println("TASKS:\n" + ftasks);
//        System.err.println("----------------------------------------");
        Set<Integer> output_dependencies = new HashSet<Integer>();
        FragmentTaskMessage local_ftask = null;
        for (FragmentTaskMessage ftask : ftasks) {
            assertEquals(FragmentTaskMessage.USER_PROC, ftask.getFragmentTaskType());
            
            // All tasks for the multi-partition query should have exactly one output with no inputs
            if (!ftask.hasInputDependencies()) {
                assertEquals("FragmentTaskMessage for multi-partition query does not have the right # of fragments", 1, ftask.getFragmentCount());
                for (int i = 0, cnt = ftask.getFragmentCount(); i < cnt; i++) {
                    assertEquals(ExecutionSite.NULL_DEPENDENCY_ID, ftask.getOnlyInputDepId(i));
                } // FOR
                assertNotNull(ftask.getOutputDependencyIds());
                assertEquals(1, ftask.getOutputDependencyIds().length);
                output_dependencies.add(ftask.getOutputDependencyIds()[0]);
            } else {
                assertNull("Already have local task:\n" + local_ftask, local_ftask);
                local_ftask = ftask;
            }
//            System.err.println("Partition #" + partition);
//            System.err.println(Arrays.asList(p_ftasks.get(partition)));
//            System.err.println();
        } // FOR
        assertNotNull(local_ftask);

        assertEquals("Local partition does not have the right # of fragments", 1, local_ftask.getFragmentCount());
        int with_input_dependencies = 0;
        // All the local partition's tasks should output something
        // System.err.println(local_ftask);
        assertNotNull(local_ftask.getOutputDependencyIds());
        assertEquals(1, local_ftask.getOutputDependencyIds().length);

        // Check that one of them needs input and that it outputs something
        // that the other partitions are not outputting (i.e., data for the client)
        if (local_ftask.hasInputDependencies()) {
            with_input_dependencies++;
            assertEquals(output_dependencies.size(), local_ftask.getInputDependencyCount());
            for (int i = 0, cnt = local_ftask.getFragmentCount(); i < cnt; i++) {
                int input_dependency = local_ftask.getOnlyInputDepId(i);
                assert(output_dependencies.contains(input_dependency));
            } // FOR
            assertFalse(output_dependencies.contains(local_ftask.getOutputDependencyIds()[0]));
        }
    }
}