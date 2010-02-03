/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.iloggr.client.model.Application;
import com.iloggr.client.model.ApplicationDuration;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.LocationFix;
import com.iloggr.client.model.Phone;
import com.iloggr.client.model.UserReportData;
import com.iloggr.client.services.ReportingService;
import com.iloggr.server.JSON.JSONRPC;
import com.iloggr.util.HibernateUtil;

/**
 * Records events in the database.
 *
 * @author eliot
 * @version 1.0
 *
 */
public class EventManager {

	SimpleDateFormat mysqldf = JSONRPC.getDateFormat(); // used for parsing
	SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // TODO Eliot: should be excel CSV friendly


	// Set up a simple configuration that logs on the console.
	static final Logger log = Logger.getLogger(EventManager.class);

	private Session session;


	public EventManager() {
		this.session = HibernateUtil.getSessionFactory().openSession();
	}

	public EventManager(Session session) {
		if (this.session != null && this.session != session) this.closeSession();
		this.session = session;
	}

	@Override
	protected void finalize() throws Throwable {
		closeSession();
		super.finalize(); // not necessary if extending Object.
	}

	public void closeSession() {
		if (session.isOpen())
			session.close();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}



	public Event getEvent(long id) {
		Event event = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		try {
			event = (Event) session.get(Event.class, id);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager: getEvent (id) failed: " + e.toString());
		}
		return event;
	}

	public void saveEvent(Event event) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			Phone phone = event.getPhone();
			if (phone != null) {
				phone.addEvent(event);
				saveOrUpdatePhone(phone);
			}
			session.save(event);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager saveEvent failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

	}
	
	public void saveApplicationDuration(ApplicationDuration appDuration) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.save(appDuration);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager saveApplicationDuration failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

	}


	public void saveLocationFix(LocationFix lf) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.save(lf);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager saveLocationFix failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

	}



	public void saveOrUpdateEvent(Event event) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			Phone phone = event.getPhone();
			if (phone != null) {
				phone.addEvent(event);
				saveOrUpdatePhone(phone);
			}
			session.saveOrUpdate(event);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log
			.info("EventManager saveOrUpdateEventfailed: "
					+ e.toString());
			throw e;
		}

	}

	public void savePhone(Phone phone) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.save(phone);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager savePhone failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

	}


	public void saveOrUpdatePhone(Phone phone) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.saveOrUpdate(phone);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager saveOrUpdatePhone failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

	}

	public Phone getPhoneByClientID(String clientID) {
		if (clientID == null) return null;
		Phone phone = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<Phone> results = session.createCriteria(Phone.class).add(
					Restrictions.eq("clientID", clientID)).list();
			if (results.size() > 1)
				log.info("**** more than two phones with same client ID: " + clientID);
			phone = results.get(0);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager: getPhoneByClientID failed: " + e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
		}
		return phone;
	}


	public LocationFix recordLocationFix(Phone phone, double latitude, double longitude, double accuracy, Date timeOfFix) throws Exception {
		LocationFix lf = new LocationFix(0l, latitude, longitude, accuracy, timeOfFix, phone);
		saveLocationFix(lf);
		return lf;
	}


	public Event recordEvent(Phone phone, int eventType, Application application, Date when, String description, double data, double latitude, double longitude) throws Exception {
		Event event = new Event(0l, eventType, application, when, description, data, phone, latitude, longitude);
		saveEvent(event);
		return event;
	}

	public Event recordEvent(Phone phone, int eventType, Application application, Date when, String description, double data) throws Exception {
		Event event = new Event(0l, eventType, application, when, description, data, phone, Event.NOLAT, Event.NOLON);
		saveEvent(event);
		return event;
	}
	
	public void saveOrUpdateApplicationCounter(Counter c) throws Exception  {
	}

	
	public void incrementCounter(Counter counter) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			// TODO:  will not work with a large number of concurrent access (no blocked read)
			counter.setCount(counter.getCount()+1);
			session.saveOrUpdate(counter);
			if (!existsTransaction) {
				session.getTransaction().commit();
			}
		} catch (Exception e) {
				log.info("ProvisioningManager saveOrUpdateApplicationCounter: " + e.toString());
				session.getTransaction().rollback();
				throw e;
		}
		
	}


	/**
	 * Will do a search for events given an app and date range, sort and limit.
	 *
	 * @param app The application
	 * @param from The beginning date
	 * @param to The end date
	 * @param desc Descending or Ascending order on date
	 * @param limit Number of records to retrieve
	 * @return The raw events for the given criteria
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Event> getEvents(Application app, Date from, Date to, boolean desc, int limit, int offset) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		ArrayList<Event>results = new ArrayList<Event>();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			String query = getEventQuery(app, from, to, desc, limit, offset);
			SQLQuery qs = session.createSQLQuery(query).addEntity(Event.class);
			results = (ArrayList<Event>) qs.list();
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager getEvents failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

		return results;

	}

	public List<UserReportData> fetchUniquePhoneReport(Application app, Date from, Date to, int unit) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		ArrayList<UserReportData>results = new ArrayList<UserReportData>();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			String query = getUniquePhoneQuery(app, from, to, unit);
			//q.xd, count(q.phone)
			SQLQuery qs = session.createSQLQuery(query)
			.addScalar("q.xd", Hibernate.STRING)
			.addScalar("count(q.phone)", Hibernate.LONG);
			List<Object[]> records = qs.list();
			for (Object[] record : records) {
				UserReportData data = new UserReportData((String) record[0], (Long)record[1]);
				results.add(data);
			}
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager fetchUniquePhoneReport failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

		return results;

	}
	
	public List<Event> fetchRegionReport(Application app, Date from, Date to) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		List<Event>results = null;
		if (!existsTransaction)
			session.beginTransaction();
		try {
			String query = getReqionReportQuery(app, from, to);
			//q.xd, count(q.phone)
			SQLQuery qs = session.createSQLQuery(query)
			.addEntity(Event.class);
			results = qs.list();
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager fetchRegionReport failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}
		return results;
	}
	

	public List<UserReportData> fetchUniquePhoneStats(Application app, Date from, Date to, int unit) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		ArrayList<UserReportData>results = new ArrayList<UserReportData>();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			String query = getUniquePhoneStatsQuery(app, from, to, unit);
			SQLQuery qs = session.createSQLQuery(query)
			.addScalar("max(c)", Hibernate.LONG)
			.addScalar("min(c)", Hibernate.LONG)
			.addScalar("avg(c)", Hibernate.LONG);
			List<Object[]> records = qs.list();
			if (records.size() == 0) return results;
			//  should just be one summary record returned form the query
			Object[] stats = records.get(0);
			// could contain nulls if no stats
			if (stats[0] == null) return results;
			String unitName = ReportingService.UNIT_NAMES[unit];
			UserReportData data = new UserReportData("Max Users/"+unitName, (Long)stats[0]);
			results.add(data);
			data = new UserReportData("Min Users/"+unitName, (Long)stats[1]);
			results.add(data);
			data = new UserReportData("Avg Users/"+unitName, (Long)stats[2]);
			results.add(data);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager fetchUniquePhoneReport failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

		return results;

	}
	
	public List<UserReportData> fetchUniquePhoneDurationStats(Application app, Date from, Date to, int unit) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		ArrayList<UserReportData>results = new ArrayList<UserReportData>();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			String query = getUniquePhoneDurationStatsQuery(app, from, to, unit);
			SQLQuery qs = session.createSQLQuery(query)
			.addScalar("max(seconds_duration)", Hibernate.LONG)
			.addScalar("min(seconds_duration)", Hibernate.LONG)
			.addScalar("avg(seconds_duration)", Hibernate.LONG);
			List<Object[]> records = qs.list();
			if (records.size() == 0) return results;
			//  should just be one summary record returned form the query
			Object[] stats = records.get(0);
			if (stats[0] == null) return results;
			String unitName = ReportingService.UNIT_NAMES[unit];
			UserReportData data = new UserReportData("Max Duration(s)/"+unitName, (Long)stats[0]);
			results.add(data);
			data = new UserReportData("Min Duration(s)/"+unitName, (Long)stats[1]);
			results.add(data);
			data = new UserReportData("Avg Duration(s)/"+unitName, (Long)stats[2]);
			results.add(data);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager fetchUniquePhoneDurationStats failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

		return results;

	}

	@SuppressWarnings("unchecked")
	public String getEventsCSV(Application app, Date from, Date to, boolean desc, int limit, int offset) throws Exception {
		StringBuffer result = new StringBuffer();
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			//query.append("Select e.id, e.record_time, p.id, e.description, e.data, l.latitude, l.longitude from event e, phone p, location_fix f where e.application =");
			String query = getEventQueryCSV(app, from, to, desc, limit, offset);
			SQLQuery qs = session.createSQLQuery(query)
			.addScalar("event.id", Hibernate.BIG_INTEGER)
			.addScalar("event.record_time", Hibernate.DATE)
			.addScalar("phone.clientid", Hibernate.STRING)
			.addScalar("event.description", Hibernate.STRING)
			.addScalar("event.data", Hibernate.STRING)
			.addScalar("event.latitude", Hibernate.DOUBLE)
			.addScalar("event.longitude", Hibernate.DOUBLE);
			List<Object[]> data = qs.list();
			int count = data.size();
			result.append(eventCSVHeaders());
			result.append('\n');
			for (Object[] record : data) {
				result.append(eventCSVSerialize(record));
				if (--count > 0) result.append(",");
				result.append('\n');
			}
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("EventManager getEventsCSV failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

		return result.toString();

	}

	private String getEventQuery(Application app, Date from, Date to, boolean desc, int limit, int offset) {
		StringBuffer query = new StringBuffer();
		query.append("Select * from event where application =");
		query.append(app.getId());
		query.append(" and record_time between TIMESTAMP(");
		query.append(mysqldf.format(from));
		query.append(") and TIMESTAMP(");
		query.append(mysqldf.format(to));
		query.append(") order by record_time ");
		query.append((desc)?"desc":"asc");
		query.append(" limit ");
		query.append(limit);
		query.append(" offset ");
		query.append(offset);

		return query.toString();

	}

	private String getEventQueryCSV(Application app, Date from, Date to, boolean desc, int limit, int offset) {
		StringBuffer query = new StringBuffer();
		query.append("select event.id, event.record_time, phone.clientid, event.description, event.data, event.latitude, event.longitude from phone left join event on event.phone = phone.id where application=");
		//		query.append("Select e.id, e.record_time, p.clientid, e.description, e.data, f.latitude, f.longitude from event e, phone p, location_fix f where e.application =");
		query.append(app.getId());
		query.append(" and event.record_time");
		query.append(relativeDateSubquery(from, to));
		query.append(" order by event.record_time ");
		query.append((desc)?"desc":"asc");
		query.append(" limit ");
		query.append(limit);
		query.append(" offset ");
		query.append(offset);

		return query.toString();

	}

	private String relativeDateSubquery(Date from, Date to) {
		StringBuffer query = new StringBuffer();
		query.append(" between TIMESTAMP(");
		query.append(mysqldf.format(from));
		query.append(") and TIMESTAMP(");
		query.append(mysqldf.format(to));
		query.append(")");
		return query.toString();
	}

	//		select q.application, q.xd, count(q.phone) from (select distinct application, DATE_FORMAT(record_time, '%y%m%d') as xd, phone from event) as q, application app where q.application = app.id and appID='B2' group by q.xd ;

	private String getUniquePhoneQuery(Application app, Date from, Date to, int unit) {
		StringBuffer query = new StringBuffer();
		query.append("select q.xd, count(q.phone) from (select distinct DATE_FORMAT(record_time, '");
		query.append(ReportingService.UNIT_FORMATS[unit]);
		query.append("') as xd, phone from event where application =");
		query.append(app.getId());
		query.append(" and record_time");
		query.append(relativeDateSubquery(from, to));
		query.append(" ) as q group by q.xd");
		return query.toString();
	}
	
	private String getReqionReportQuery(Application app, Date from, Date to) {
		StringBuffer query = new StringBuffer();
		query.append("select * from event where application =");
		query.append(app.getId());
		query.append(" and record_time");
		query.append(relativeDateSubquery(from, to));
		query.append(" and event_type = ");
		query.append(Event.EVENT_TYPE_LAUNCH);
		return query.toString();
	}
	

	/**
	 * Will generate min, max, and avg unique users per HOUR for the date range provided.
	 * 
	 * @param app Application object
	 * @param from Start date for report
	 * @param to End date for report
	 * @param unit The unit to report as a MySql date format string (see ReportingService.java)
	 * 
	 * @return
	 */
	private String getUniquePhoneStatsQuery(Application app, Date from, Date to, int unit) {
		StringBuffer query = new StringBuffer();
		query.append("select min(c), max(c), avg(c) from (select q.xd as d, count(q.phone) as c from (select distinct DATE_FORMAT(record_time, '");
		query.append(ReportingService.UNIT_FORMATS[unit]);
		query.append("') as xd, phone from event where application = ");
		query.append(app.getId());
		query.append(" and record_time");
		query.append(relativeDateSubquery(from, to));
		query.append(" ) as q group by q.xd) as counts");
		return query.toString();
	}
	
	private String getUniquePhoneDurationStatsQuery(Application app, Date from, Date to, int unit) {
		StringBuffer query = new StringBuffer();
		query.append("select min(seconds_duration), max(seconds_duration), avg(seconds_duration), DATE_FORMAT(record_time, '");
		query.append(ReportingService.UNIT_FORMATS[unit]);
		query.append("') as xd from application_duration where application = ");
		query.append(app.getId());
		query.append(" and record_time");
		query.append(relativeDateSubquery(from, to));
		query.append(" group by xd");
		return query.toString();
	}
	
/*select event.id, event.record_time, phone.clientid, event.description, event.data, event.latitude, event.longitude from phone left join event on event.phone = phone.id where application=1 and event.record_time between TIMESTAMP(20090813060000) and TIMESTAMP(20090818055959) order by event.record_time desc limit 30 offset 0;

select count(distinct phone) from event;
select count(distinct phone) from event where DATE_SUB(CURDATE(),INTERVAL 10 DAY) <= record_time;
*/



	private String eventCSVHeaders() {
		return "Event ID, Date/Time of Event, Client ID, Description, Data, Latitude, Longitude"; 
	}

	private String eventCSVSerialize(Object[] record) {
		StringBuffer result = new StringBuffer();
		result.append(record[0]);
		result.append(",");
		result.append(displayFormat.format(record[1]));
		result.append(",");
		result.append('"');
		result.append(record[2]);
		result.append('"');
		result.append(",");
		result.append('"');
		result.append(record[3]);
		result.append('"');
		result.append(",");
		result.append(record[4]);
		result.append(",");
		result.append(record[5]);
		result.append(",");
		result.append(record[6]);
		return result.toString();
	}








}