package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Time;

import po.ReportXML;

public interface TransReport extends Remote {

	public abstract void uploadReport(ReportXML r)  throws RemoteException;
	public abstract ReportXML downloadReport(Time t, String name) throws RemoteException;
}
