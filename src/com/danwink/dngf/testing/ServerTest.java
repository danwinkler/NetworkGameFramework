package com.danwink.dngf.testing;

import com.danwink.dngf.DNGFServer;

public class ServerTest extends DNGFServer<TestEnum>
{
	public void update( float d )
	{
		
	}

	public void handleMessage( int sender, TestEnum type, Object message )
	{
		
	}

	public void onConnect( int id ) 
	{
		
	}

	public void onDisconnect( int id ) 
	{
		
	}
	
	public static void main( String[] args )
	{
		ServerTest st = new ServerTest();
		st.addClasses( TestClassRegister.classes );
		st.begin();
	}
}
