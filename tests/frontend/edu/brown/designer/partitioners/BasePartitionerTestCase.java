package edu.brown.designer.partitioners;

import java.io.File;

import edu.brown.BaseTestCase;
import edu.brown.correlations.ParameterCorrelations;
import edu.brown.designer.Designer;
import edu.brown.designer.DesignerHints;
import edu.brown.designer.DesignerInfo;
import edu.brown.statistics.WorkloadStatistics;
import edu.brown.utils.ProjectType;
import edu.brown.workload.AbstractWorkload;
import edu.brown.workload.WorkloadTraceFileOutput;
import edu.brown.workload.filters.ProcedureLimitFilter;

public abstract class BasePartitionerTestCase extends BaseTestCase {

    private static final long WORKLOAD_XACT_LIMIT = 1000;
    private static final int NUM_THREADS = 1;
    protected static final int NUM_PARTITIONS = 10;
    protected static final int NUM_INTERVALS = 10;

    // Reading the workload takes a long time, so we only want to do it once
    protected static AbstractWorkload workload;
    protected static WorkloadStatistics stats;
    protected static ParameterCorrelations correlations;
    protected static File correlations_file;

    protected Designer designer;
    protected DesignerInfo info;
    protected DesignerHints hints;

    
    @Override
    protected void setUp(ProjectType type, boolean fkeys) throws Exception {
        super.setUp(type, fkeys);
        
        this.addPartitions(NUM_PARTITIONS);

        // Super hack! Walk back the directories and find out workload directory
        if (workload == null) {
            File workload_file = this.getWorkloadFile(type);
            assertNotNull(workload_file);
            assert(workload_file.exists());
            workload = new WorkloadTraceFileOutput(catalog);
            AbstractWorkload.Filter filter = new ProcedureLimitFilter(WORKLOAD_XACT_LIMIT);
            ((WorkloadTraceFileOutput) workload).load(workload_file.getAbsolutePath(), catalog_db, filter);
            
            File stats_file = this.getStatsFile(type);
            assertNotNull(stats_file);
            assert(stats_file.exists());
            stats = new WorkloadStatistics(catalog_db);
            stats.load(stats_file.getAbsolutePath(), catalog_db);
            
            correlations_file = this.getCorrelationsFile(type);
            assertNotNull(correlations_file);
            assert(correlations_file.exists());
            correlations = new ParameterCorrelations();
            correlations.load(correlations_file.getAbsolutePath(), catalog_db);
        }
        
        // Setup everything else (that's just how we roll up in this ma)
        this.info = new DesignerInfo(catalog_db, workload);
        this.info.setStats(stats);
        this.info.setCorrelations(correlations);
        this.info.setCorrelationsFile(correlations_file.getAbsolutePath());
        this.info.setNumThreads(NUM_THREADS);
        this.info.setNumIntervals(NUM_INTERVALS);
        
        this.hints = new DesignerHints();
    }
}
