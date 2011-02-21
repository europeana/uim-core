package eu.europeana.uim.logging.database;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** An user entity with an unique username and arbitrary password.
 * 
 * @author ajuffing
 */
@Entity
@Table(name="uim_logentry")
@javax.persistence.SequenceGenerator(name="SEQ_UIM_LOGENTRY",sequenceName="seq_uim_logentry")
public class DatabaseLogEntry implements Serializable {

    /** */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_UIM_LOGENTRY")
    private Long oid;

    @Column
    private String level;

    @Column
    private Date time;

    @Column
    private String workflow;
    
    @Column
    private String plugin;
    
    @Column
    private String dataset;
    
    @Column
    private String record;
    
    @Column
    private Date starttime;

    @Column
    private Date endtime;

    @Column(length=4000) 
    private String description;
    
    
	public Long getOid() {
		return oid;
	}




	public String getLevel() {
		return level;
	}




	public void setLevel(String level) {
		this.level = level;
	}




	public Date getTime() {
		return time;
	}




	public void setTime(Date time) {
		this.time = time;
	}




	public String getWorkflow() {
		return workflow;
	}




	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}



	public String getPlugin() {
		return plugin;
	}




	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}




	public String getDataset() {
		return dataset;
	}




	public void setDataset(String dataset) {
		this.dataset = dataset;
	}




	public Date getStarttime() {
		return starttime;
	}




	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}




	public Date getEndtime() {
		return endtime;
	}




	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}




	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}




	public String getRecord() {
		return record;
	}




	public void setRecord(String record) {
		this.record = record;
	}


	
}
