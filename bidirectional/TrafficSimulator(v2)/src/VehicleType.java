
public class VehicleType {

        private double stripWidth = 0.5;
        
        //returns width of vehicle based on type
	public double Width(int type)
	{
		if(type == 1)
			return 1.76;
		else if(type == 2 )
			return  1.78;
		else if(type == 3 )
			return  2.13;
		else if(type == 4 )
			return  2.02;
		else if(type == 5 )
			return  1.8;
		else if(type == 6 )
			return  1.3;
		else if(type == 7 )
			return  1.22;
		else if(type == 8 )
			return  0.75;
		else if(type == 9 )
			return  0.61;
		else if(type == 10 )
			return  2.46;
		else if(type == 11 )
			return  2.44;
		else
			return 1.76;
	}
        
        //returns length of vehicle based on type
	public double Length(int type)
	{
		if(type == 1)
			return 4.54;
		else if(type == 2 )
			return  4.29;
		else if(type == 3 )
			return  4.47;
		else if(type == 4 )
			return  5.78;
		else if(type == 5 )
			return  5.5;
		else if(type == 6 )
			return  2.63;
		else if(type == 7 )
			return  2.51;
		else if(type == 8 )
			return  2.13;
		else if(type == 9 )
			return  1.78;
		else if(type == 10 )
			return  8.46;
		else if(type == 11 )
			return  6.7;
		else
			return 4.54;
	}
        
        //returns number of strips a vehicle occupies for a given type
        public int numStrip(int type)
	{
                double width = Width(type);
		double widthCon = width/stripWidth;
		int numStrip = (int) Math.ceil(widthCon);
		return numStrip;
	}


}
