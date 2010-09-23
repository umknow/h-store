/***************************************************************************
 *  Copyright (C) 2009 by H-Store Project                                  *
 *  Brown University                                                       *
 *  Massachusetts Institute of Technology                                  *
 *  Yale University                                                        *
 *                                                                         *
 *  Original Version:                                                      *
 *  Zhe Zhang (zhe@cs.brown.edu)                                           *
 *                                                                         *
 *  Modifications by:                                                      *
 *  Andy Pavlo (pavlo@cs.brown.edu)                                        *
 *  http://www.cs.brown.edu/~pavlo/                                        *
 *                                                                         *
 *  Permission is hereby granted, free of charge, to any person obtaining  *
 *  a copy of this software and associated documentation files (the        *
 *  "Software"), to deal in the Software without restriction, including    *
 *  without limitation the rights to use, copy, modify, merge, publish,    *
 *  distribute, sublicense, and/or sell copies of the Software, and to     *
 *  permit persons to whom the Software is furnished to do so, subject to  *
 *  the following conditions:                                              *
 *                                                                         *
 *  The above copyright notice and this permission notice shall be         *
 *  included in all copies or substantial portions of the Software.        *
 *                                                                         *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        *
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF     *
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. *
 *  IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR      *
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,  *
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR  *
 *  OTHER DEALINGS IN THE SOFTWARE.                                        *
 ***************************************************************************/
package edu.brown.benchmark.tm1;

import edu.brown.benchmark.AbstractProjectBuilder;
import edu.brown.benchmark.tm1.procedures.*;

public class TM1ProjectBuilder extends AbstractProjectBuilder {

    public static final Class<?> PROCEDURES[] = new Class<?>[] {
        // Benchmark Specification
        DeleteCallForwarding.class,
        GetAccessData.class,
        GetNewDestination.class,
        GetSubscriberData.class,
        InsertCallForwarding.class,
        UpdateLocation.class,
        UpdateSubscriberData.class,
        
        // Testing Procedures
        InsertSubscriber.class,
        GetTableCounts.class,
    };
    
    public static final String PARTITIONING[][] = 
        new String[][] {
            {"SUBSCRIBER", "S_ID"},
            {"ACCESS_INFO", "S_ID"},
            {"SPECIAL_FACILITY", "S_ID"},
            {"CALL_FORWARDING", "S_ID"},
        };
    
    public static final Class<?> SUPPLMENTALS[] = new Class<?>[] {
        // Nothing
    };
    
    public TM1ProjectBuilder() {
        super("tm1", TM1ProjectBuilder.class, PROCEDURES, PARTITIONING, SUPPLMENTALS, true);
    }
}