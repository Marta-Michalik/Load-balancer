from mininet.topo import Topo
class MyTopo( Topo ):
   "Simple topology example."
   def __init__( self ):
      "Create custom topo."

      # Initialize topology
      Topo.__init__( self )
      # Add hosts and switches
      leftHost1 = self.addHost( 'h1', mac='00:00:00:00:00:01' )
      leftHost2 = self.addHost( 'h2', mac='00:00:00:00:00:02' )
      leftHost3 = self.addHost( 'h3', mac='00:00:00:00:00:03' )
      rightHost1  = self.addHost( 'h4', mac='00:00:00:00:00:04' )
      rightHost2 = self.addHost( 'h5', mac='00:00:00:00:00:05' )
      rightHost3 = self.addHost( 'h6', mac='00:00:00:00:00:06' )
      sw1 = self.addSwitch( 's1', dpid="0000000000000011" )
      sw2 = self.addSwitch( 's2', dpid="0000000000000012" )
      sw3 = self.addSwitch( 's3', dpid="0000000000000013" )
      sw4 = self.addSwitch( 's4', dpid="0000000000000014" )
      sw5 = self.addSwitch( 's5', dpid="0000000000000015" )
      sw6 = self.addSwitch( 's6', dpid="0000000000000016" )
      # Add links
      self.addLink( leftHost1, sw1 )
      self.addLink( leftHost2, sw1 )
      self.addLink( leftHost3, sw1 )
      self.addLink( sw4, rightHost1 )
      self.addLink( sw4, rightHost2 )
      self.addLink( sw4, rightHost3 )
      self.addLink( sw1, sw2 )
      self.addLink( sw2, sw3 )
      self.addLink( sw3, sw4 )
      self.addLink( sw4, sw5 )
      self.addLink( sw5, sw6 )
      self.addLink( sw6, sw1 )
topos = { 'mytopo': ( lambda: MyTopo() ) }
