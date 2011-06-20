/**
 * 
 */
package eu.europeana.uim.repoxclient.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Function;

import eu.europeana.uim.repoxclient.plugin.RepoxRestClient;
import eu.europeana.uim.repoxclient.command.RepoxPluginCommand.Operation;

/**
 * 
 * 
 * @author Georgios Markakis
 */
@Command(name = "uim", scope = "repoxagent")
public class RepoxPluginCommand implements Function, Action{

	enum Operation {info,createagregator,deleteagregator,updateagregator,
		createprovider,deleteprovider,updateprovider,
		createdatasource,deletedatasource,updatedatasource,
		retrieveaggregators,retrieveproviders,retrievedatasources,
		initiateharvesting,getharvestingstatus,getactiveharvests,
		addpollinglistener,removepollinglisteners}
	
	private RepoxRestClient repoxservice;
	
	@Option(name = "-o", aliases = {"--operation"}, required = false)
	private Operation operation;
	
	@Argument(index = 0)
	private String argument0;

	@Argument(index = 1)
	private String argument1;
	
	@Argument(index = 2)
	private String argument2;
	
	/**
	 * @param repoxservice
	 */
	public RepoxPluginCommand(RepoxRestClient repoxservice){
		this.repoxservice = repoxservice;
	}
	

	
	/* (non-Javadoc)
	 * @see org.apache.felix.gogo.commands.Action#execute(org.apache.felix.service.command.CommandSession)
	 */
	@Override
	public Object execute(CommandSession commandsession) throws Exception {

		PrintStream out = commandsession.getConsole();
	    BufferedReader in = new BufferedReader(new InputStreamReader(commandsession.getKeyboard()));
		
		if (operation == null) {
			out.println("Please specify an operation with the '-o' option. Possible values are:");
			out.println("info                                                       \t\t\t\t Connection info to Sugarcrm");
			out.println("createagregator <aggr_name>                          \t\t\t\t Creates a new session for the client");					
			out.println("deleteagregator <aggr_name>                                   \t\t\t\t Retrieves records by status");
			out.println("updateagregator <aggr_name>                                     \t\t\t\t Retrieves a record by id");
			out.println("createprovider  <provider_name,aggr_name>                                    \t\t\t\t Updates a record by id");
			out.println("deleteprovider  <provider_name>                       \t\t\t\t Changes a record status by id");
			out.println("updateprovider  <provider_name>                      \t\t\t\t Initialize a workflow by id");
			out.println("createdatasource  <datasource_name,provider_name>  \t\t\t\t Initializes workflows according ot records states ");
			out.println("deletedatasource <datasource_name>                           \t\t\t\t Creates Collection/Providers objects from a record");
			out.println("updatedatasource <datasource_name>                         \t\t\t\t Adds a note attachment to a specific record");
			out.println("retrieveaggregators                                         \t\t\t\t Adds a sample polling listener to UIM");
			out.println("retrieveproviders                                     \t\t\t\t Removes all polling Listeners");
			out.println("retrievedatasources                                    \t\t\t\t Updates a record by id");
			out.println("initiateharvesting <datasource_name>                       \t\t\t\t Changes a record status by id");
			out.println("getharvestingstatus  <datasource_name>                 \t\t\t\t Initialize a workflow by id");
			out.println("getactiveharvests                                       \t\t\t\t Initializes workflows according ot records states ");
			out.println("addpollinglistener <datasource_name>                           \t\t\t\t Creates Collection/Providers objects from a record");
			out.println("removepollinglisteners <datasource_name>                         \t\t\t\t Adds a note attachment to a specific record");
			
			return null;
		}
	    
		return null;
	}

	
	
	/* (non-Javadoc)
	 * @see org.apache.felix.service.command.Function#execute(org.apache.felix.service.command.CommandSession, java.util.List)
	 */
	@Override
	public Object execute(CommandSession arg0, List<Object> arg1)
			throws Exception {
		return null;
	}

}
