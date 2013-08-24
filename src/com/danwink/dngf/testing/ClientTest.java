package com.danwink.dngf.testing;

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;

import javax.vecmath.Point2f;
import javax.vecmath.Point2i;

import com.danwink.dngf.DNGFClient;

public class ClientTest extends DNGFClient<TestEnum>
{
	HashMap<Integer, Point2i> players = new HashMap<Integer, Point2i>();
	 
	public void setup()
	{
		
	}
	
	public static void main( String[] args )
	{
		ClientTest ct = new ClientTest();
		ct.addClasses( TestClassRegister.classes );
		ct.setServerClass( ServerTest.class );
		ct.begin();
	}

	public void handleMessage( TestEnum type, Object o )
	{
		switch( type )
		{
		case POSITION:
			int[] arr = (int[])o;
			int id = arr[0];
			int x = arr[1];
			int y = arr[2];
			Point2i p = players.get( id );
			if( p == null )
			{
				p = new Point2i( x, y );
				players.put( id, p );
			}
			else
			{
				p.x = x;
				p.y = y;
			}
			break;
		case REMOVE:
			int idToRemove = (Integer)o;
			players.remove( idToRemove );
			break;
		}
	}

	public void update( float d )
	{
		
	}
	
	public void render()
	{
		Set<Integer> keySet = players.keySet();
		
		color( Color.WHITE );
		fillRect( 0, 0, getWidth(), getHeight() );
		
		color( Color.black );
		for( Integer i : keySet )
		{
			Point2i p = players.get( i );
			fillOval( p.x - 10, p.y - 10, 20, 20 );
		}
	}
}
