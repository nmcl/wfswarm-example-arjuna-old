
package com.arjuna.webservices11.wsarjtx.sei;

import com.arjuna.schemas.ws._2005._10.wsarjtx.NotificationType;
import com.arjuna.schemas.ws._2005._10.wsarjtx.TerminationCoordinatorPortType;
import com.arjuna.services.framework.task.Task;
import com.arjuna.services.framework.task.TaskManager;
import com.arjuna.webservices.logging.WSTLogger;
import com.arjuna.webservices11.wsarj.ArjunaContext;
import com.arjuna.webservices11.wsarjtx.processors.TerminationCoordinatorProcessor;
import com.arjuna.webservices11.SoapFault11;
import org.jboss.ws.api.addressing.MAP;
import com.arjuna.webservices11.wsaddr.AddressingHelper;
import com.arjuna.webservices.SoapFault;
import org.xmlsoap.schemas.soap.envelope.Fault;

import javax.annotation.Resource;
import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.handler.MessageContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 *
 */
@WebService(name = "TerminationCoordinatorPortType", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx",
        // wsdlLocation = "/WEB-INF/wsdl/wsarjtx-termination-coordinator-binding.wsdl",
        serviceName = "TerminationCoordinatorService",
        portName = "TerminationCoordinatorPortType"
)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@HandlerChain(file="/ws-t_handlers.xml")
@Addressing(required=true)
public class TerminationCoordinatorPortTypeImpl implements TerminationCoordinatorPortType
{

    @Resource
     private WebServiceContext webServiceCtx;

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CompleteOperation", action = "http://schemas.arjuna.com/ws/2005/10/wsarjtx/Complete")
    @Oneway
    public void completeOperation(
        @WebParam(name = "Complete", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx", partName = "parameters")
        NotificationType parameters)
    {
        if (WSTLogger.logger.isTraceEnabled()) {
            WSTLogger.logger.trace(getClass().getSimpleName() + " constructor");
        }

        MessageContext ctx = webServiceCtx.getMessageContext();
        HttpServletRequest request = (HttpServletRequest)ctx.get(MessageContext.SERVLET_REQUEST);
        final NotificationType complete = parameters;
        final MAP inboundMap = AddressingHelper.inboundMap(ctx);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                TerminationCoordinatorProcessor.getProcessor().complete(complete, inboundMap, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CloseOperation", action = "http://schemas.arjuna.com/ws/2005/10/wsarjtx/Close")
    @Oneway
    public void closeOperation(
        @WebParam(name = "Close", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx", partName = "parameters")
        NotificationType parameters)
    {
        if (WSTLogger.logger.isTraceEnabled()) {
            WSTLogger.logger.trace(getClass().getSimpleName() + ".closeOperation");
        }

        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType close = parameters;
        final MAP inboundMap = AddressingHelper.inboundMap(ctx);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                TerminationCoordinatorProcessor.getProcessor().close(close, inboundMap, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CancelOperation", action = "http://schemas.arjuna.com/ws/2005/10/wsarjtx/Cancel")
    @Oneway
    public void cancelOperation(
        @WebParam(name = "Cancel", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx", partName = "parameters")
        NotificationType parameters)
    {
        if (WSTLogger.logger.isTraceEnabled()) {
            WSTLogger.logger.trace(getClass().getSimpleName() + ".cancelOperation");
        }

        MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType cancel = parameters;
        final MAP inboundMap = AddressingHelper.inboundMap(ctx);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                TerminationCoordinatorProcessor.getProcessor().cancel(cancel, inboundMap, arjunaContext) ;
            }
        }) ;
    }

    @WebMethod(operationName = "fault", action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/fault")
    @Oneway
    public void fault(
            @WebParam(name = "Fault", targetNamespace = "http://schemas.xmlsoap.org/soap/envelope/", partName = "parameters")
            Fault fault)
    {
        if (WSTLogger.logger.isTraceEnabled()) {
            WSTLogger.logger.trace(getClass().getSimpleName() + ".fault");
        }

        MessageContext ctx = webServiceCtx.getMessageContext();
        final MAP inboundMap = AddressingHelper.inboundMap(ctx);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);
        final SoapFault soapFault = SoapFault11.fromFault(fault);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                TerminationCoordinatorProcessor.getProcessor().soapFault(soapFault, inboundMap, arjunaContext); ;
            }
        }) ;
    }
}
