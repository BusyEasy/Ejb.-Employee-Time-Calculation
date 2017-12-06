package ejbTimer;

import java.io.IOException;

import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;

import org.json.JSONException;

/**
 * Session Bean implementation class EjbTimer
 */
@Startup
@Singleton
@LocalBean
public class EjbTimer implements EjbInf{

    /**
     * Default constructor. 
     */
    public EjbTimer() {
        // TODO Auto-generated constructor stub
    }

    @Schedule(second = "5", minute = "35", hour = "9")
	@Override
	public void EjbTime(Timer timer) {
    	
    	Tessss a =new Tessss();
    	try {
			a.GetTwoRequest();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
