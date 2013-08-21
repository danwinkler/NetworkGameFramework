package com.danwink.dngf;

import java.util.List;
import java.util.ArrayList;

import com.danwink.dngf.DNGFInternalMessage.DNGFInternalMessageType;
import com.phyloa.dlib.network.DMessage;
import com.phyloa.dlib.network.DServer;

public abstract class DNGFServer<E extends Enum>
{
	DServer server;
	
	private int writeBuffer = 1024;
	private int objectBuffer = 4096;
	
	private int portTCP = 35123;
	private int portUDP = 35125;
	
	ArrayList<Class> classesToRegister = new ArrayList<Class>(); 
	
	ServerLoop sl;
	
	boolean inLobby = false;
	
	public void begin()
	{
		server = new DServer( writeBuffer, objectBuffer );
		for( Class c : classesToRegister )
		{
			server.register( c );
		}
		server.register( DNGFMessage.class );
		server.register( DNGFInternalMessage.class );
		server.register( DNGFInternalMessageType.class );
		server.start( 31456, 31457 );
		
		Thread t = new Thread( sl = new ServerLoop() );
		t.start();
	}
	
	void internalUpdate( float d )
	{
		while( server.hasServerMessages() )
		{
			DMessage dm = server.getNextServerMessage();
			
			if( dm.message instanceof DNGFInternalMessage )
			{
				
			}
			else
			{
				DNGFMessage<E> message = (DNGFMessage<E>)dm.message;
				switch( dm.messageType )
				{
				case CONNECTED:
					server.sendToClient( dm.sender, new DNGFInternalMessage( inLobby ? DNGFInternalMessageType.LOBBY : DNGFInternalMessageType.PLAY, null ) );
					if( inLobby )
					{
						
					}
					else
					{
						
						onConnect( dm.sender );
					}
					break;
				case DISCONNECTED:
					onDisconnect( dm.sender );
					break;
				case DATA:
					handleMessage( dm.sender, message.type, message.message );
					break;
				}
			}
		}
		
		if( inLobby )
		{
			
		}
		else
		{
			update( d );
		}
	}
	
	public void addClasses( Class... classes )
	{
		for( Class c : classes )
		{
			classesToRegister.add( c );
		}
	}
	
	public void sendOne( int id, E e, Object o )
	{
		
	}
	
	public void sendAll( E e, Object o )
	{
		
	}
	
	public abstract void update( float d );
	public abstract void handleMessage( int sender, E type, Object message );
	public abstract void onConnect( int id );
	public abstract void onDisconnect( int id );
	
	public class ServerLoop implements Runnable 
	{
		long lastTime;
		long frameTime = (1000 / 30);
		long timeDiff;
		public boolean running = true;
		public ServerLoop()
		{
			
		}

		public void run() 
		{
			lastTime = System.currentTimeMillis();
			long lastWholeFrame = lastTime;
			while( running )
			{
				try{
				long timeDiff = System.currentTimeMillis() - lastWholeFrame;
				lastWholeFrame = System.currentTimeMillis();
				update( 1000.f / timeDiff );
				} catch( Exception ex )
				{
					ex.printStackTrace();
				}
				long time = System.currentTimeMillis();
				timeDiff = (lastTime + frameTime) - time;
				if( timeDiff > 0 )
				{
					try {
						Thread.sleep( timeDiff );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lastTime = System.currentTimeMillis();
			}
			server.stop();
		}	
	}

	public int getWriteBuffer() 
	{
		return writeBuffer;
	}

	public void setWriteBuffer( int writeBuffer )
	{
		this.writeBuffer = writeBuffer;
	}

	public int getObjectBuffer() 
	{
		return objectBuffer;
	}

	public void setObjectBuffer( int objectBuffer )
	{
		this.objectBuffer = objectBuffer;
	}

	public int getPortTCP() 
	{
		return portTCP;
	}

	public void setPortTCP( int portTCP ) 
	{
		this.portTCP = portTCP;
	}

	public int getPortUDP() 
	{
		return portUDP;
	}

	public void setPortUDP( int portUDP )
	{
		this.portUDP = portUDP;
	}

	public void stop() 
	{
		sl.running = false;
	}
}
