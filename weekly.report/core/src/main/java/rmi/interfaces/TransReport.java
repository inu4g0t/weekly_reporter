package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Time;

import po.Report;

public interface TransReport extends Remote {

	public abstract void uploadReport(Report r)  throws RemoteException;
	public abstract Report downloadReport(Time t, String name) throws RemoteException;
}
