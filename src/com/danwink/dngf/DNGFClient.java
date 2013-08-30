package com.danwink.dngf;

import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;

import com.danwink.dngf.DNGFInternalMessage.DNGFInternalMessageType;
import com.danwink.dngf.screens.DNGFConnectScreen;
import com.danwink.dngf.screens.DNGFHomeScreen;
import com.danwink.dngf.screens.DNGFPlayScreen;
import com.phyloa.dlib.network.DClient;
import com.phyloa.dlib.network.DServer;
import com.phyloa.dlib.renderer.DScreen;
import com.phyloa.dlib.renderer.DScreenHandler;
import com.phyloa.dlib.renderer.Graphics2DRenderer;

public abstract class DNGFClient<E extends Enum> extends Graphics2DRenderer
{	
	Class serverClass;
	
	ArrayList<Class> classesToRegister = new ArrayList<Class>();
	
	DScreenHandler<DNGFClient<E>, Graphics2DRenderer> dsh = new DScreenHandler<DNGFClient<E>, Graphics2DRenderer>();
	
	private int writeBuffer = 102400;
	private int objectBuffer = 409600;
	
	int portTCP = 35123;
	int portUDP = 35125;
	
	public DNGFServer<E> server;
	public DClient client;
	
	//settings
	public String startScreen = "home";
	public DScreen homeScreen;
	public DScreen connectScreen;
	
	public void initialize()
	{
		size( 1280, 800 );
		
		//set defaults
		homeScreen = new DNGFHomeScreen( this );
		connectScreen = new DNGFConnectScreen();
		
		//allow for changes
		setup();
		
		//now that settings are set, create everything
		dsh.register( "home", homeScreen );
		dsh.register( "connect", connectScreen );
		dsh.register( "play", (DScreen)new DNGFPlayScreen() );
		
		dsh.activate( startScreen, this );
	}

	public void update()
	{
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		//TODO add timing
		dsh.update( this, 10 );
		dsh.render( this, this );
	}
	
	public abstract void setup();
	
	public abstract void handleMessage( E type, Object o );
	
	public abstract void update( float d );
	
	public void addClasses( Class... classes )
	{
		for( Class c : classes )
		{
			classesToRegister.add( c );
		}
	}
	
	public void setServerClass( Class serverClass )
	{
		this.serverClass = serverClass;
	}
	
	public Class getServerClass()
	{
		return serverClass;
	}

	public int getWriteBuffer() {
		return writeBuffer;
	}

	public void setWriteBuffer(int writeBuffer) {
		this.writeBuffer = writeBuffer;
	}

	public int getObjectBuffer() {
		return objectBuffer;
	}

	public void setObjectBuffer(int objectBuffer) {
		this.objectBuffer = objectBuffer;
	}
	
	public void send( E type, Object message )
	{
		client.sendToServer( new DNGFMessage<E>( type, message ) );
	}

	public void startClient( String address ) throws IOException 
	{
		if( client != null )
		{
			client.stop();
		}
		client = new DClient( writeBuffer, objectBuffer );
		for( Class c : classesToRegister )
		{
			client.register( c );
		}
		client.register( DNGFMessage.class );
		client.register( DNGFInternalMessage.class );
		client.register( DNGFInternalMessageType.class );
		client.start( address, 1000, portTCP, portUDP );
	}
}
