package com.danwink.dngf;

import java.util.ArrayList;

import com.danwink.dngf.screens.DNGFHomeScreen;
import com.phyloa.dlib.renderer.DScreen;
import com.phyloa.dlib.renderer.DScreenHandler;
import com.phyloa.dlib.renderer.Graphics2DRenderer;

public abstract class DNGFClient<E extends Enum> extends Graphics2DRenderer
{	
	Class serverClass;
	
	ArrayList<Class> classesToRegister = new ArrayList<Class>();
	
	DScreenHandler<DNGFClient<E>, Graphics2DRenderer> dsh = new DScreenHandler<DNGFClient<E>, Graphics2DRenderer>();
	
	public DNGFServer<E> server;
	
	//settings
	public String startScreen = "home";
	public DScreen homeScreen;
	
	public void initialize()
	{
		//set defaults
		homeScreen = new DNGFHomeScreen( this );
		
		//allow for changes
		setup();
		
		//now that settings are set, create everything
		dsh.register( "home", homeScreen );
		
		
		dsh.activate( startScreen, this );
	}

	public void update()
	{
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
}
