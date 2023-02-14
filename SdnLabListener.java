package pl.edu.agh.kt;



import java.util.Collection;



import java.util.Map;



import org.projectfloodlight.openflow.protocol.OFMessage;

import org.projectfloodlight.openflow.protocol.OFPacketIn;

import org.projectfloodlight.openflow.protocol.OFType;

import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;

import org.projectfloodlight.openflow.types.MacAddress;

import org.projectfloodlight.openflow.types.OFPort;



import net.floodlightcontroller.core.FloodlightContext;

import net.floodlightcontroller.core.IOFMessageListener;

import net.floodlightcontroller.core.IOFSwitch;

import net.floodlightcontroller.core.module.FloodlightModuleContext;

import net.floodlightcontroller.core.module.FloodlightModuleException;

import net.floodlightcontroller.core.module.IFloodlightModule;

import net.floodlightcontroller.core.module.IFloodlightService;



import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;

import java.util.ArrayList;



import org.slf4j.Logger;

import org.slf4j.LoggerFactory;



import pl.edu.agh.kt.ServerObtainer;



public class SdnLabListener implements IFloodlightModule, IOFMessageListener {



	protected IFloodlightProviderService floodlightProvider;

	protected static Logger logger;

	private static boolean withInterval = false;
	private ARP arp;



	@Override

	public String getName() {

		return SdnLabListener.class.getSimpleName();

	}



	@Override

	public boolean isCallbackOrderingPrereq(OFType type, String name) {

		// TODO Auto-generated method stub

		return false;

	}



	@Override

	public boolean isCallbackOrderingPostreq(OFType type, String name) {

		// TODO Auto-generated method stub

		return false;

	}



	@Override

	public net.floodlightcontroller.core.IListener.Command receive(

			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {



		//logger.info("************* NEW PACKET IN *************");

		PacketExtractor extractor = new PacketExtractor();

		extractor.packetExtract(cntx);



		// TODO LAB 6

		OFPacketIn pin = (OFPacketIn) msg;

		OFPort outPort = OFPort.of(0);
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		if(eth.getEtherType().equals(EthType.ARP)){
			arp = (ARP)eth.getPayload();
			Flows.arpreply(arp, sw);
		}
		else if (eth.getEtherType().equals(EthType.IPv4)) {
	
		if (sw.getId().toString().matches("00:00:00:00:00:00:00:11")) {
			logger.info("s1");	
			if (pin.getInPort().getPortNumber() == 1

					|| pin.getInPort().getPortNumber() == 2

					|| pin.getInPort().getPortNumber() == 3) {

				outPort = OFPort.of(4);

				if(pin.getInPort().getPortNumber() == 1) {
					Flows.simpleAdd(sw, pin, cntx, outPort);				
				}
				
				

				else {
					Flows.simpleAddSource(sw, pin, cntx, outPort,
							IPv4Address.of("10.0.0.1"));
				}

			} else if (pin.getInPort().getPortNumber() == 5) {

				outPort = OFPort.of(ServerObtainer.getServer(withInterval));

				logger.info("Wybrany port  : " +  outPort);



				if (outPort == OFPort.of(2)) {
					Flows.simpleAdd(sw, pin, cntx, outPort,

							IPv4Address.of("10.0.0.2"),

							MacAddress.of("00:00:00:00:00:02"));

				} else if (outPort == OFPort.of(3)) {
					Flows.simpleAdd(sw, pin, cntx, outPort,

							IPv4Address.of("10.0.0.3"),

							MacAddress.of("00:00:00:00:00:03"));

				} else {

					Flows.simpleAdd(sw, pin, cntx, outPort);

				}

			}


		} else if (sw.getId().toString().matches("00:00:00:00:00:00:00:12")) {

			if (pin.getInPort().getPortNumber() == 1) {

				outPort = OFPort.of(2);
				logger.info("sw 2");
				Flows.simpleAdd(sw, pin, cntx, outPort);

			}


		} else if (sw.getId().toString().matches("00:00:00:00:00:00:00:13")) {



			if (pin.getInPort().getPortNumber() == 1) {

				outPort = OFPort.of(2);
				logger.info("sw 3");

				Flows.simpleAdd(sw, pin, cntx, outPort);

			}

			//logger.info("3Pin:" + pin.getInPort() + "*********  Outport:"

			//		+ outPort + "....");

		} else if (sw.getId().toString().matches("00:00:00:00:00:00:00:14")) {

			if (pin.getInPort().getPortNumber() == 1

					|| pin.getInPort().getPortNumber() == 2

					|| pin.getInPort().getPortNumber() == 3) {

				outPort = OFPort.of(5);

				Flows.simpleAdd(sw, pin, cntx, outPort);

			}
			logger.info("sw 4");
			
			if (pin.getInPort().getPortNumber() == 4) {
				outPort = OFPort.of(1);
				
				
		            /* We got an IPv4 packet; get the payload from Ethernet */
		        IPv4 ipv4 = (IPv4) eth.getPayload();
		            /* Various getters and setters are exposed in IPv4 */
		        byte[] ipOptions = ipv4.getOptions();
		        IPv4Address ipadd = ipv4.getDestinationAddress();
		    
		 
		        if(ipadd.toString().equals("10.0.0.4")){
		        	outPort = OFPort.of(1);
					logger.info("wracam do 10.0.0.4");
					Flows.simpleAdd(sw, pin, cntx, outPort,
					IPv4Address.of("10.0.0.4"),
					MacAddress.of("00:00:00:00:00:04"));
		        }
				if(ipadd.toString().equals("10.0.0.5")){
					outPort = OFPort.of(2);
					logger.info("wracam do 10.0.0.5");
					Flows.simpleAdd(sw, pin, cntx, outPort,
					IPv4Address.of("10.0.0.5"),
					MacAddress.of("00:00:00:00:00:05"));
				}
				if(ipadd.toString().equals("10.0.0.6")){
					outPort = OFPort.of(3);
					Flows.simpleAdd(sw, pin, cntx, outPort,
					IPv4Address.of("10.0.0.6"),
					MacAddress.of("00:00:00:00:00:06"));
					logger.info("wracam do 10.0.0.6");
				}
				
			

			}

			//logger.info("4Pin:" + pin.getInPort() + "*********  Outport:"

			//		+ outPort + "....");

		} else if (sw.getId().toString().matches("00:00:00:00:00:00:00:15")) {

			if (pin.getInPort().getPortNumber() == 1) {

				outPort = OFPort.of(2);
				logger.info("sw 5");
				Flows.simpleAdd(sw, pin, cntx, outPort);

			}

			//logger.info("5Pin:" + pin.getInPort() + "*********  Outport:"

			//		+ outPort + "....");

		} else if (sw.getId().toString().matches("00:00:00:00:00:00:00:16")) {

			if (pin.getInPort().getPortNumber() == 1) {

				outPort = OFPort.of(2);
				logger.info("sw 6");
				Flows.simpleAdd(sw, pin, cntx, outPort);

			}

			//logger.info("6Pin:" + pin.getInPort() + "*********  Outport:"

			//		+ outPort + "....");

		}



		// Flows.simpleAdd2(sw, outPort);

		}

		return Command.STOP;

	}
	



	@Override

	public Collection<Class<? extends IFloodlightService>> getModuleServices() {

		// TODO Auto-generated method stub

		return null;

	}



	@Override

	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {

		// TODO Auto-generated method stub

		return null;

	}



	@Override

	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {

		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();

		l.add(IFloodlightProviderService.class);

		return l;

	}



	@Override

	public void init(FloodlightModuleContext context)

			throws FloodlightModuleException {

		floodlightProvider = context

				.getServiceImpl(IFloodlightProviderService.class);

		logger = LoggerFactory.getLogger(SdnLabListener.class);

	}



	@Override

	public void startUp(FloodlightModuleContext context)

			throws FloodlightModuleException {

		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);

		//logger.info("******************* START **************************");



	}



}
