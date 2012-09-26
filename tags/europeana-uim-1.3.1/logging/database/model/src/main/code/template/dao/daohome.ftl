${pojo.getPackageDeclaration()}
// Generated ${date} by Hibernate Tools ${version}
// Template by: Andreas Juffinger

<#assign classbody>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>

/**
 * Home object for domain model class ${declarationName}.
 * @see ${pojo.getQualifiedDeclarationName()}
 * @author Hibernate Tools
 */
 
public class ${declarationName}Home {

    private static final ${pojo.importType("java.util.logging.Logger")} log = Logger.getLogger(${pojo.getDeclarationName()}Home.class.getName());
    
    @${pojo.importType("javax.persistence.PersistenceContext")}
    protected ${pojo.importType("javax.persistence.EntityManager")} entityManager;

    public void setEntityManager(${pojo.importType("javax.persistence.EntityManager")} entityManager) {
    	this.entityManager = entityManager;
    }
    
    public ${pojo.importType("javax.persistence.EntityManager")} getEntityManager() {
       	return this.entityManager;
    }
    
    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
    public ${declarationName} update(${declarationName} entity) {
        return getEntityManager().merge(entity);
    }
    
    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
    public void insert(${declarationName}... entities) {
    	if (entities != null)  {
    		for (${declarationName} entity : entities) {
        		getEntityManager().persist(entity);
        	}
    	}
    }

    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
    public boolean delete(${declarationName}... entities) {
   		boolean deleted = false;
    	if (entities != null)  {
    		for (${declarationName} entity : entities) {
		    	if (entity.getOid() != null) {
		    		// need to reattach detached entity
		    		entity = getEntityManager().merge(entity);
		    		getEntityManager().remove(entity);
		    		deleted |= true;
		    	}
		    }
		}
        return deleted;
    }


    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
    public int truncate(){
   		${pojo.importType("javax.persistence.Query")} query = getEntityManager().createQuery("DELETE FROM ${declarationName}");
        return query.executeUpdate();
    }


    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
    public int execute(String statement){
   		${pojo.importType("javax.persistence.Query")} query = getEntityManager().createQuery(statement);
        return query.executeUpdate();
    }


    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
    public ${declarationName} findByOid(Long oid) {
    	if (oid == null) return null;
        try {
            return getEntityManager().find(${declarationName}.class, oid);
        } catch (${pojo.importType("javax.persistence.NoResultException")} exc) {
            log.fine("${declarationName} not found for oid: " + oid);
            return null;
        }
    }
    

<#foreach queryName in cfg.namedQueries.keySet()>
<#if queryName.startsWith(clazz.entityName + ".")>
<#assign querydef = cfg.namedQueries.get(queryName)>

<#if (querydef.parameterTypes)??>
<#assign params = querydef.parameterTypes>
<#assign methname = c2j.unqualify(queryName)>
<#assign argList = c2j.asFinderArgumentList(params, pojo)>

<#if jdk5 && methname.startsWith("find")>
    @SuppressWarnings("unchecked")
    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
    public ${pojo.importType("java.util.List")}<${declarationName}> ${methname}(${argList}) {
<#elseif methname.startsWith("count")>
    public int ${methname}(${argList}) {
<#else>
    public ${pojo.importType("java.util.List")} ${methname}(${argList}) {
</#if>
        ${pojo.importType("javax.persistence.Query")} query = getEntityManager().createNamedQuery("${queryName}");
<#foreach param in params.keySet()>
        query.setParameter("${param}", ${param});
</#foreach>
<#if jdk5 && methname.startsWith("find")>
        return (List<${declarationName}>) query.getResultList();
<#elseif methname.startsWith("count")>
        return ( (Integer) query.getSingleResult() ).intValue();
<#else>
        return query.getResultList();
</#if>
    }



<#if jdk5 && methname.startsWith("find")>
    @SuppressWarnings("unchecked")
    @${pojo.importType("org.springframework.transaction.annotation.Transactional")}
	<#if argList.length() &gt; 0>
    public ${pojo.importType("java.util.List")}<${declarationName}> ${methname}(${argList}, int start, int limit) {
    <#else>
    public ${pojo.importType("java.util.List")}<${declarationName}> ${methname}(int start, int limit) {
    </#if>
<#elseif methname.startsWith("count")>
	<#if argList.length() &gt; 0>
    public int ${methname}(${argList}, int start, int limit) {
    <#else>
    public int ${methname}(int start, int limit) {
    </#if>
<#else>
	<#if argList.length() &gt; 0>
    public ${pojo.importType("java.util.List")} ${methname}(${argList}, int start, int limit) {
    <#else>
    public ${pojo.importType("java.util.List")} ${methname}(int start, int limit) {
    </#if>
</#if>
        ${pojo.importType("javax.persistence.Query")} query = getEntityManager().createNamedQuery("${queryName}");
<#foreach param in params.keySet()>
        query.setParameter("${param}", ${param});
</#foreach>
        query.setMaxResults(limit);
        query.setFirstResult(start);
<#if jdk5 && methname.startsWith("find")>
        return (List<${declarationName}>) query.getResultList();
<#elseif methname.startsWith("count")>
        return ( (Integer) query.getSingleResult() ).intValue();
<#else>
        return query.getResultList();
</#if>
    }
<#else>
    // no parameter given for: ${queryName}
</#if>
</#if>

</#foreach>
}
</#assign>

${pojo.generateImports()}
${classbody}
