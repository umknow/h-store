/**
 * 
 */
package edu.brown.utils;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author pavlo
 *
 */
public class StringAppender implements Appender {

    private StringBuilder sb;
    private String name;
    
    /**
     * 
     */
    public StringAppender() {
        this.sb = new StringBuilder();
    }
    
    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#doAppend(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    public void doAppend(LoggingEvent arg0) {
        this.sb.append(arg0.getMessage()).append("\n");
    }
    
    public void clear() {
        this.sb = new StringBuilder();
    }
    
    @Override
    public String toString() {
        return this.sb.toString();
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#addFilter(org.apache.log4j.spi.Filter)
     */
    @Override
    public void addFilter(Filter arg0) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#clearFilters()
     */
    @Override
    public void clearFilters() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#close()
     */
    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#getErrorHandler()
     */
    @Override
    public ErrorHandler getErrorHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#getFilter()
     */
    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#getLayout()
     */
    @Override
    public Layout getLayout() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#requiresLayout()
     */
    @Override
    public boolean requiresLayout() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#setErrorHandler(org.apache.log4j.spi.ErrorHandler)
     */
    @Override
    public void setErrorHandler(ErrorHandler arg0) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#setLayout(org.apache.log4j.Layout)
     */
    @Override
    public void setLayout(Layout arg0) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#setName(java.lang.String)
     */
    @Override
    public void setName(String arg0) {
        this.name = arg0;
    }
}
