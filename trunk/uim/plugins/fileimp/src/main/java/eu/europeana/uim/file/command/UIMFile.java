package eu.europeana.uim.file.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Function;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.command.ConsoleProgressMonitor;
import eu.europeana.uim.common.parse.XMLStreamParserException;
import eu.europeana.uim.file.MDRStreamLoader;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Request;

/**
 * Reads from file content to the given storage.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@Command(name = "uim", scope = "file")
public class UIMFile implements Function, Action {
    private enum Format {
        ESE, DCX
    }

    @Argument(required = true)
    private String   filename;

    @Option(name = "-c", aliases = { "--collection" }, required = true)
    private String   collection;

    @Option(name = "-f", aliases = { "--format" })
    private Format   format = Format.ESE;

    private Registry registry;

    /**
     * Creates a new instance of this class.
     */
    public UIMFile() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public UIMFile(Registry registry) {
        this.registry = registry;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        return execute(session, null);
    }

    @Override
    public Object execute(CommandSession commandSession, List<Object> arguments) throws Exception {
        StorageEngine<?> storage = registry.getStorage();
        Object[] ids = execute(storage, commandSession);
        if (ids == null) {
            commandSession.getConsole().println(
                    "Failed to read any records from: <" + filename + ">");
        } else {
            commandSession.getConsole().println(
                    "Read " + ids.length + " records from: <" + filename + ">");
        }
        return null;
    }

    /**
     * Method which uses the specified fields (filename, format, collection) to actually call the
     * doImport method and read the content fromt the file into the defined storage.
     * 
     * @param <I>
     *            generic ID
     * 
     * @param storage
     *            storage engines which is the target of the file content
     * @param commandSession
     *            stream to which log/info output goes
     * @return imported IDs
     * @throws XMLStreamParserException
     * @throws FileNotFoundException
     * @throws StorageEngineException
     */
    public <I> I[] execute(StorageEngine<I> storage, CommandSession commandSession)
            throws XMLStreamParserException, FileNotFoundException, StorageEngineException {
        File file = new File(filename);
        if (file.exists()) {
            InputStream f = new FileInputStream(file);

            Collection<I> targetcoll = storage.findCollection(collection);
            if (targetcoll != null) {
                Request<I> request = storage.createRequest(targetcoll, new Date());
                storage.updateRequest(request);
                MDRStreamLoader reader = new MDRStreamLoader();
                switch (format) {
                case ESE:
                default:
                    return reader.doEseImport(f, storage, request, new ConsoleProgressMonitor(
                            commandSession.getConsole()));
                }
            } else {
                commandSession.getConsole().println("Collection: <" + collection + "> not found.");
            }
        } else {
            commandSession.getConsole().println("File: <" + file.getAbsolutePath() + "> not found.");
        }
        return null;
    }

    /**
     * @return the registry
     */
    public Registry getRegistry() {
        return registry;
    }
}
