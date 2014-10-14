package com.piropa_field_test;

public class DataSave {
    
    //private variables
	static int stat_id=0;
    int _id;
    int _strength;
    int _Lac;
    int _Cid;
    String _Latitude;
    String _Longitude;
    
     
    // Empty constructor
    public DataSave(){
         
    }
    // constructor
    public DataSave(int id, int strength, String latitude, String longitude, int Lac, int Cid){
        this._id = id;
        this._strength = strength;
        this._Latitude = latitude;
        this._Longitude = longitude;
        this._Lac = Lac;
        this._Cid = Cid;
    }
     
    // constructor
    public DataSave(int strength, String latitude, String longitude, int Lac, int Cid){
    	this._id = stat_id;
        this._strength = strength;
        this._Latitude = latitude;
        this._Longitude = longitude;
        this._Lac = Lac;
        this._Cid = Cid;
        this.stat_id++;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
 
    public int getStrength(){
        return this._strength;
    }
     
  
    public void setStrength(int strength){
        this._strength = strength;
    }
     
  
    public String getLatitude(){
        return this._Latitude;
    }
     
 
    public void setLatitude(String latitude){
        this._Latitude = latitude;
    }
    
    
    public String getLongitude(){
        return this._Longitude;
    }
     
 
    public void setLongitude(String longitude){
        this._Longitude = longitude;
    }
    
    
    public int getLac(){
        return this._Lac;
    }
     
 
    public void setLac(int Lac){
        this._Lac = Lac;
    }
    
    public int getCid(){
        return this._Cid;
    }
     
 
    public void setCid(int Cid){
        this._Cid = Cid;
    }

    public String getAll(){
    	return ("lac: " + _Lac + ", cid: "+ _Cid + "  Koordinaten: " + _Latitude + "," + _Longitude + "   Stärke: " + _strength);
    }
    
    public void TakePoint(){
    }
    



}
