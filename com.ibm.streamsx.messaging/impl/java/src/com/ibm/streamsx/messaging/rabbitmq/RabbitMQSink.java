/*******************************************************************************
* Copyright (C) 2015, MOHAMED-ALI SAID
* All Rights Reserved
*******************************************************************************/
/* Generated by Streams Studio: March 26, 2014 11:37:11 AM EDT */
package com.ibm.streamsx.messaging.rabbitmq;

import java.lang.Thread.State;
import java.util.List;

import com.ibm.streams.operator.AbstractOperator;
import com.ibm.streams.operator.OperatorContext;
import com.ibm.streams.operator.StreamingData.Punctuation;
import com.ibm.streams.operator.StreamingInput;
import com.ibm.streams.operator.Tuple;
import com.ibm.streams.operator.model.InputPortSet;
import com.ibm.streams.operator.model.InputPorts;
import com.ibm.streams.operator.model.Libraries;
import com.ibm.streams.operator.model.Parameter;
import com.ibm.streams.operator.model.PrimitiveOperator;


import com.ibm.streamsx.messaging.rabbitmq.RabbitMQWrapper;
import org.slf4j.LoggerFactory;

/**
 * This operator was originally contributed by Mohamed-Ali Said @saidmohamedali
 * Class for an operator that consumes tuples and does not produce an output stream. 
 * This pattern supports a number of input streams and no output streams. 
 * <P>
 * The following event methods from the Operator interface can be called:
 * </p>
 * <ul>
 * <li><code>initialize()</code> to perform operator initialization</li>
 * <li>allPortsReady() notification indicates the operator's ports are ready to process and submit tuples</li> 
 * <li>process() handles a tuple arriving on an input port 
 * <li>processPuncuation() handles a punctuation mark arriving on an input port 
 * <li>shutdown() to shutdown the operator. A shutdown request may occur at any time, 
 * such as a request to stop a PE or cancel a job. 
 * Thus the shutdown() may occur while the operator is processing tuples, punctuation marks, 
 * or even during port ready notification.</li>
 * </ul>
 * <p>With the exception of operator initialization, all the other events may occur concurrently with each other, 
 * which lead to these methods being called concurrently by different threads.</p> 
 */
@Libraries({ "opt/downloaded/*" })
@InputPorts(@InputPortSet(cardinality=1, optional=false, 
description=""))
@PrimitiveOperator(name="RabbitMQSink", description="something")
public class RabbitMQSink extends AbstractOperator {

	private RabbitMQWrapper rabbitMQWrapper;
	private String exchangeNameParam, routingKeyParam,userNameParam,passwordParam,hostNameParam;
	private int portIdParam;
	
	// joins to the event thread of the RFAConsumer to hold the operator open
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(RabbitMQSink.class);
	
	
	
    /**
     * Initialize this operator. Called once before any tuples are processed.
     * @param context OperatorContext for this operator.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
	@Override
	public synchronized void initialize(OperatorContext context)
			throws Exception {
    
    	// Must call super.initialize(context) to correctly setup an operator.
		super.initialize(context);
        log.trace("Operator " + context.getName() + " initializing in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );

        // TODO:
        // If needed, insert code to establish connections or resources to communicate an external system or data store.
        // The configuration information for this may come from parameters supplied to the operator invocation,
        // or external configuration files or a combination of the two.

     
        rabbitMQWrapper = new RabbitMQWrapper(exchangeNameParam);
        rabbitMQWrapper.login(userNameParam, passwordParam, hostNameParam, portIdParam);
        // TODO:
        // If needed, insert code to establish connections or resources to communicate an external system or data store.
        // The configuration information for this may come from parameters supplied to the operator invocation, 
        // or external configuration files or a combination of the two.
        // all initialization completed successfully?
    }
	
    /**
     * Notification that initialization is complete and all input and output ports 
     * are connected and ready to receive and submit tuples.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public synchronized void allPortsReady() throws Exception {
    	// This method is commonly used by source operators. 
    	// Operators that process incoming tuples generally do not need this notification. 
        OperatorContext context = getOperatorContext();
        log.trace("Operator " + context.getName() + " all ports are ready in PE: " + context.getPE().getPEId() + " in Job: " + context.getPE().getJobId() );

        
    }

    /**
     * Process an incoming tuple that arrived on the specified port.
     * @param stream Port the tuple is arriving on.
     * @param tuple Object representing the incoming tuple.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public void process(StreamingInput<Tuple> stream, Tuple tuple)
            throws Exception {
        // TODO Insert code here to process the incoming tuple, 
    	// typically sending tuple data to an external system or data store.
    	
    	String message = tuple.getString("message");
    	String guid = tuple.getString("guid_request");
    	
         rabbitMQWrapper.publish(guid,message);
		
    	
    }
    
    /**
     * Process an incoming punctuation that arrived on the specified port.
     * @param stream Port the punctuation is arriving on.
     * @param mark The punctuation mark
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public void processPunctuation(StreamingInput<Tuple> stream,
    		Punctuation mark) throws Exception {
    	// TODO: If window punctuations are meaningful to the external system or data store, 
    	// insert code here to process the incoming punctuation.
    }
    
    @Parameter(optional=true, description="Exchange Name.")
	public void setExchangeName(String value) {
		exchangeNameParam = value;
	}
    
    @Parameter(optional=true, description="Exchange Name.")
	public void setRoutingKey(String value) {
		routingKeyParam = value;
	}
    
    @Parameter(optional=true, description="Exchange Name.")
	public void setUsername(String value) {
		userNameParam = value;
	}
    
    @Parameter(optional=true, description="Exchange Name.")
	public void setPassword(String value) {
		passwordParam = value;
	}
    
    @Parameter(optional=true, description="Exchange Name.")
	public void setHostname(String value) {
		hostNameParam = value;
	}
    
    @Parameter(optional=true, description="Exchange Name.")
	public void setPortId(int value) {
		portIdParam = value;
	}

    /**
     * Shutdown this operator.
     * @throws Exception Operator failure, will cause the enclosing PE to terminate.
     */
    @Override
    public synchronized void shutdown() throws Exception {
        OperatorContext context = getOperatorContext();
       
        rabbitMQWrapper.logout(); // should force the join() to exit
        
        // TODO: If needed, close connections or release resources related to any external system or data store.
        super.shutdown();
    }
    
}
