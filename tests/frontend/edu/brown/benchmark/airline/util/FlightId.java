package edu.brown.benchmark.airline.util;

import java.util.Date;

import edu.brown.benchmark.airline.AirlineConstants;

public class FlightId {
    
    private static final int MAX_VALUE = 65535; // 2^14 - 1
    private static final int VALUE_OFFSET = 16;

    private final long id;
    private final long depart_airport_id;
    private final long arrive_airport_id;
    private final Long encoded;
    
    /**
     * This is the departure time of the flight in minutes since the benchmark start date
     * @see BenchmarkProfile.getFlightTimeInMinutes()
     */
    private final long depart_date;

    /**
     * Constructor
     * @param id - the unique id for this depart + arrive airport combination
     * @param depart_airport_id - the id of the departure airport
     * @param arrive_airport_id - the id of the arrival airport
     * @param benchmark_start - the base date of when the benchmark data starts (including past days)
     * @param flight_date - when departure date of the flight
     */
    public FlightId(long id, long depart_airport_id, long arrive_airport_id, Date benchmark_start, Date flight_date) {
        this.id = id;
        this.depart_airport_id = depart_airport_id;
        this.arrive_airport_id = arrive_airport_id;
        this.depart_date = FlightId.calculateFlightDate(benchmark_start, flight_date);
        this.encoded = FlightId.encode(new long[]{  this.id,
                                                    this.depart_airport_id,
                                                    this.arrive_airport_id,
                                                    this.depart_date});
    }
    
    /**
     * Constructor. Converts a composite id generated by encode() into the full object
     * @param composite_id
     */
    public FlightId(long composite_id) {
        long values[] = FlightId.decode(composite_id);
        this.id = values[0];
        this.depart_airport_id = values[1];
        this.arrive_airport_id = values[2];
        this.depart_date = values[3];
        this.encoded = composite_id;
    }
    
    /**
     * 
     * @param benchmark_start
     * @param flight_date
     * @return
     */
    protected static final long calculateFlightDate(Date benchmark_start, Date flight_date) {
        return (flight_date.getTime() - benchmark_start.getTime()) / 3600000; // 60s * 60m * 1000
    }
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the depart_airport_id
     */
    public long getDepartAirportId() {
        return depart_airport_id;
    }

    /**
     * @return the arrive_airport_id
     */
    public long getArriveAirportId() {
        return arrive_airport_id;
    }

    /**
     * @return the flight departure date
     */
    public Date getDepartDate(Date benchmark_start) {
        return (new Date(benchmark_start.getTime() + (this.depart_date * 3600000)));
    }
    
    public boolean isUpcoming(Date benchmark_start, long past_days) {
        Date depart_date = this.getDepartDate(benchmark_start);
        return ((depart_date.getTime() - benchmark_start.getTime()) >= (past_days * AirlineConstants.MILISECONDS_PER_DAY)); 
    }
    
    public long encode() {
        return (this.encoded);
    }

    public static long encode(long...values) {
        assert(values.length == 4);
        for (int i = 0; i < values.length; i++) {
            assert(values[i] >= 0) : "FlightId value at position " + i + " is " + values[i];
            assert(values[i] < MAX_VALUE) : "FlightId value at position " + i + " is " + values[i] + ". Max value is " + MAX_VALUE;
        } // FOR
        
        long id = values[0];
        int offset = VALUE_OFFSET;
        // System.out.println("0: " + id);
        for (int i = 1; i < values.length; i++) {
            id = id | values[i]<<offset;
            // System.out.println(id + ": " + id + "  [offset=" + offset + ", value=" + values[i] + "]");
            offset += VALUE_OFFSET;
        }
        return (id);
    }
    
    public static long[] decode(long composite_id) {
        long values[] = new long[4];
        int offset = 0;
        for (int i = 0; i < values.length; i++) {
            values[i] = composite_id>>offset & MAX_VALUE;
            offset += VALUE_OFFSET;
        } // FOR
        return (values);
    }
    
    @Override
    public String toString() {
        return ("FlightId<" + 
                "depart_airport=" + this.depart_airport_id + "," +
                "arrive_airport=" + this.arrive_airport_id + "," +
                "depart_date=" + this.depart_date + "," +
                "id=" + this.id + ">");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FlightId) {
            FlightId o = (FlightId)obj;
            return (this.id == o.id &&
                    this.depart_airport_id == o.depart_airport_id &&
                    this.arrive_airport_id == o.arrive_airport_id &&
                    this.depart_date == o.depart_date);
        }
        return (false);
    }
    
    @Override
    public int hashCode() {
        return (this.encoded.hashCode());
    }
    
}
