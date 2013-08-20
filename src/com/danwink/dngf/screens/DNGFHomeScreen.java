package com.danwink.dngf.screens;

import java.awt.Color;

import com.danwink.dngf.DNGFClient;
import com.danwink.dngf.DNGFServer;
import com.phyloa.dlib.dui.AWTComponentEventMapper;
import com.phyloa.dlib.dui.DButton;
import com.phyloa.dlib.dui.DPanel;
import com.phyloa.dlib.dui.DUI;
import com.phyloa.dlib.dui.DUIEvent;
import com.phyloa.dlib.dui.DUIListener;
import com.phyloa.dlib.renderer.DScreen;
import com.phyloa.dlib.renderer.DScreenHandler;
import com.phyloa.dlib.renderer.Graphics2DRenderer;

public class DNGFHomeScreen extends DScreen<DNGFClient, Graphics2DRenderer> implements DUIListener
{
	DNGFClient client;
	
	DUI dui;
	
	DPanel panel;
	
	DButton startLocalServer;
	DButton joinGame;
	
	public DNGFHomeScreen( DNGFClient client )
	{
		this.client = client;
		AWTComponentEventMapper em = new AWTComponentEventMapper();
		em.register( client.canvas );
		dui = new DUI( em );
		
		panel = new DPanel( 0, 0, 300, 300 );
		
		startLocalServer = new DButton( "Start Local Server", 0, 0, 300, 100 );
		joinGame = new DButton( "Join Game", 0, 100, 300, 100 );
		
		panel.add( startLocalServer );
		panel.add( joinGame );
		
		dui.add( panel );
		
		dui.addDUIListener( this );
	}
	
	public void update( DNGFClient gc, int delta )
	{
		dui.update();
	}

	public void render( DNGFClient gc, Graphics2DRenderer g )
	{
		g.color( Color.white );
		g.fillRect( 0, 0, g.getWidth(), g.getHeight() );
		
		dui.render( g );
		
		if( gc.server != null )
		{
			g.text( "running", 100, 100 );
		}
	}

	public void onActivate( DNGFClient gc, DScreenHandler<DNGFClient, Graphics2DRenderer> dsh )
	{
		panel.setLocation( gc.getWidth()/2 - 150, gc.getHeight()/2 - 200 );
		
		dui.setEnabled( true );
	}

	public void onExit() 
	{
		dui.setEnabled( false );
	}

	public void message( Object o )
	{
		
	}

	public void event( DUIEvent event ) 
	{
		if( event.getElement() == startLocalServer && event.getType() == DButton.MOUSE_UP )
		{
			if( client.server == null )
			{
				try 
				{
					client.server = (DNGFServer)client.getServerClass().newInstance();
					client.server.begin();
					startLocalServer.setText( "Stop Local Server" );
				} catch ( Exception e ) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				client.server.stop();
				client.server = null;
				startLocalServer.setText( "Start Local Server" );
			}
		}
	}
}

