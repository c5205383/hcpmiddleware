package com.hcp.objective.persistence.repositories;

import static org.springframework.data.jpa.repository.query.QueryUtils.COUNT_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.applyAndBind;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.PersistenceProvider;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Default implementation of the
 * {@link org.springframework.data.repository.CrudRepository} interface. This
 * will offer you a more sophisticated interface than the plain
 * {@link EntityManager} .
 * 
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @author Thomas Darimont
 * @param <T>
 *            the type of the entity to handle
 * @param <ID>
 *            the type of the entity's identifier
 */
// @Repository
@Transactional(readOnly = true)
public class GeneralJpaRepository<T, ID extends Serializable> implements
		JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	private final Class<T> domainClass;
	private JpaEntityInformation<T, ?> entityInformation;
	private CrudMethodMetadata crudMethodMetadata;
	
	@PersistenceContext
	protected EntityManager em;

	public GeneralJpaRepository(Class<T> domainClass) {
		this.domainClass = domainClass;
	}

	@PostConstruct
	public void lazySetupEntityInformation() {
		Assert.notNull(this.domainClass);
		this.entityInformation = JpaEntityInformationSupport.getMetadata(
				domainClass, this.em);
		Assert.notNull(this.entityInformation);
		PersistenceProvider.fromEntityManager(this.em);
	}

	/**
	 * Configures a custom {@link CrudMethodMetadata} to be used to detect
	 * {@link LockModeType}s and query hints to be applied to queries.
	 * 
	 * @param crudMethodMetadata
	 */
	public void setRepositoryMethodMetadata(
			CrudMethodMetadata crudMethodMetadata) {
		this.crudMethodMetadata = crudMethodMetadata;
	}

	protected CrudMethodMetadata getRepositoryMethodMetadata() {
		return crudMethodMetadata;
	}

	protected Class<T> getDomainClass() {
		return entityInformation.getJavaType();
	}

	private String getDeleteAllQueryString() {
		return getQueryString(DELETE_ALL_QUERY_STRING,
				entityInformation.getEntityName());
	}

	private String getCountQueryString() {

		String countQuery = String.format(COUNT_QUERY_STRING, "x", "%s");
		return getQueryString(countQuery, entityInformation.getEntityName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.CrudRepository#delete(java.io.
	 * Serializable)
	 */
	@Override
	@Transactional
	public void delete(ID id) {

		Assert.notNull(id, "The given id must not be null!");

		T entity = findOne(id);

		if (entity == null) {
			throw new EmptyResultDataAccessException(String.format(
					"No %s entity with id %s exists!",
					entityInformation.getJavaType(), id), 1);
		}

		delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.CrudRepository#delete(java.lang.Object
	 * )
	 */
	@Override
	@Transactional
	public void delete(T entity) {

		Assert.notNull(entity, "The entity must not be null!");
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable
	 * )
	 */
	@Override
	@Transactional
	public void delete(Iterable<? extends T> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		for (T entity : entities) {
			delete(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaRepository#deleteInBatch(java
	 * .lang.Iterable)
	 */
	@Override
	@Transactional
	public void deleteInBatch(Iterable<T> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		if (!entities.iterator().hasNext()) {
			return;
		}

		applyAndBind(
				getQueryString(DELETE_ALL_QUERY_STRING,
						entityInformation.getEntityName()), entities, em)
				.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.Repository#deleteAll()
	 */
	@Override
	@Transactional
	public void deleteAll() {

		for (T element : findAll()) {
			delete(element);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaRepository#deleteAllInBatch()
	 */
	@Override
	@Transactional
	public void deleteAllInBatch() {
		em.createQuery(getDeleteAllQueryString()).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.CrudRepository#findOne(java.io.
	 * Serializable)
	 */
	@Override
	public T findOne(ID id) {

		Assert.notNull(id, "The given id must not be null!");

		Class<T> domainType = getDomainClass();

		if (crudMethodMetadata == null) {
			return em.find(domainType, id);
		}

		LockModeType type = crudMethodMetadata.getLockModeType();
		Map<String, Object> hints = crudMethodMetadata.getQueryHints();

		return type == null ? em.find(domainType, id, hints) : em.find(
				domainType, id, type, hints);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaRepository#getOne(java.io.
	 * Serializable)
	 */
	@Override
	public T getOne(ID id) {

		Assert.notNull(id, "The given id must not be null!");
		return em.getReference(getDomainClass(), id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.CrudRepository#exists(java.io.
	 * Serializable)
	 */
	@Override
	public boolean exists(ID id) {

		Assert.notNull(id, "The given id must not be null!");

		if (entityInformation.getIdAttribute() == null) {
			return findOne(id) != null;
		}

		String placeholder = "x";
		String entityName = entityInformation.getEntityName();
		Iterable<String> idAttributeNames = entityInformation
				.getIdAttributeNames();
		String existsQuery = QueryUtils.getExistsQueryString(entityName,
				placeholder, idAttributeNames);

		TypedQuery<Long> query = em.createQuery(existsQuery, Long.class);

		if (!entityInformation.hasCompositeId()) {
			query.setParameter(idAttributeNames.iterator().next(), id);
			return query.getSingleResult() == 1L;
		}

		for (String idAttributeName : idAttributeNames) {

			Object idAttributeValue = entityInformation
					.getCompositeIdAttributeValue(id, idAttributeName);

			boolean complexIdParameterValueDiscovered = idAttributeValue != null
					&& !query.getParameter(idAttributeName).getParameterType()
							.isAssignableFrom(idAttributeValue.getClass());

			if (complexIdParameterValueDiscovered) {

				// fall-back to findOne(id) which does the proper mapping for
				// the parameter.
				return findOne(id) != null;
			}

			query.setParameter(idAttributeName, idAttributeValue);
		}

		return query.getSingleResult() == 1L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.repository.JpaRepository#findAll()
	 */
	@Override
	public List<T> findAll() {
		return getQuery(null, (Sort) null).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.CrudRepository#findAll(ID[])
	 */
	@Override
	public List<T> findAll(Iterable<ID> ids) {

		if (ids == null || !ids.iterator().hasNext()) {
			return Collections.emptyList();
		}

		ByIdsSpecification specification = new ByIdsSpecification();
		TypedQuery<T> query = getQuery(specification, (Sort) null);

		return query.setParameter(specification.parameter, ids).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.repository.JpaRepository#findAll(org.
	 * springframework.data.domain.Sort)
	 */
	@Override
	public List<T> findAll(Sort sort) {
		return getQuery(null, sort).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.PagingAndSortingRepository#findAll
	 * (org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<T> findAll(Pageable pageable) {

		if (null == pageable) {
			return new PageImpl<T>(findAll());
		}

		return findAll(null, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findOne
	 * (org.springframework.data.jpa.domain.Specification)
	 */
	@Override
	public T findOne(Specification<T> spec) {

		try {
			return getQuery(spec, (Sort) null).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll
	 * (org.springframework.data.jpa.domain.Specification)
	 */
	@Override
	public List<T> findAll(Specification<T> spec) {
		return getQuery(spec, (Sort) null).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll
	 * (org.springframework.data.jpa.domain.Specification,
	 * org.springframework.data.domain.Pageable)
	 */
	public Page<T> findAll(Specification<T> spec, Sort sort, Pageable pageable) {

		TypedQuery<T> query = getQuery(spec, pageable);
		return pageable == null ? new PageImpl<T>(getQuery(spec, sort)
				.getResultList()) : readPage(query, pageable, spec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll
	 * (org.springframework.data.jpa.domain.Specification,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {

		TypedQuery<T> query = getQuery(spec, pageable);
		return pageable == null ? new PageImpl<T>(query.getResultList())
				: readPage(query, pageable, spec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll
	 * (org.springframework.data.jpa.domain.Specification,
	 * org.springframework.data.domain.Sort)
	 */
	@Override
	public List<T> findAll(Specification<T> spec, Sort sort) {

		return getQuery(spec, sort).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.CrudRepository#count()
	 */
	@Override
	public long count() {
		return em.createQuery(getCountQueryString(), Long.class)
				.getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#count
	 * (org.springframework.data.jpa.domain.Specification)
	 */
	@Override
	public long count(Specification<T> spec) {

		return getCountQuery(spec).getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.CrudRepository#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public <S extends T> S save(S entity) {

		if (entityInformation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			return em.merge(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaRepository#saveAndFlush(java
	 * .lang.Object)
	 */
	@Override
	@Transactional
	public <S extends T> S saveAndFlush(S entity) {

		S result = save(entity);
		flush();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaRepository#save(java.lang.
	 * Iterable)
	 */
	@Override
	@Transactional
	public <S extends T> List<S> save(Iterable<S> entities) {

		List<S> result = new ArrayList<S>();

		if (entities == null) {
			return result;
		}

		for (S entity : entities) {
			result.add(save(entity));
		}

		return result;
	}

	public void refresh(ID id) {
		em.refresh(findOne(id));
	}

	public void refresh(T t) {
		em.refresh(t);
	}

	@SuppressWarnings("unchecked")
	public void refresh(ID... ids) {
		for (ID id : ids) {
			refresh(id);
		}
	}

	@SuppressWarnings("unchecked")
	public void refresh(T... ts) {
		for (T t : ts) {
			refresh(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.repository.JpaRepository#flush()
	 */
	@Override
	@Transactional
	public void flush() {

		em.flush();
	}

	/**
	 * Reads the given {@link TypedQuery} into a {@link Page} applying the given
	 * {@link Pageable} and {@link Specification}.
	 * 
	 * @param query
	 *            must not be {@literal null}.
	 * @param spec
	 *            can be {@literal null}.
	 * @param pageable
	 *            can be {@literal null}.
	 * @return
	 */
	protected Page<T> readPage(TypedQuery<T> query, Pageable pageable,
			Specification<T> spec) {

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		Long total = QueryUtils.executeCountQuery(getCountQuery(spec));
		List<T> content = total > pageable.getOffset() ? query.getResultList()
				: Collections.<T> emptyList();

		return new PageImpl<T>(content, pageable, total);
	}

	/**
	 * Creates a new {@link TypedQuery} from the given {@link Specification}.
	 * 
	 * @param spec
	 *            can be {@literal null}.
	 * @param pageable
	 *            can be {@literal null}.
	 * @return
	 */
	protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {

		Sort sort = pageable == null ? null : pageable.getSort();
		return getQuery(spec, sort);
	}

	/**
	 * Creates a {@link TypedQuery} for the given {@link Specification} and
	 * {@link Sort}.
	 * 
	 * @param spec
	 *            can be {@literal null}.
	 * @param sort
	 *            can be {@literal null}.
	 * @return
	 */
	protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(getDomainClass());

		Root<T> root = applySpecificationToCriteria(spec, query);
		query.select(root);

		if (sort != null) {
			query.orderBy(toOrders(sort, root, builder));
		}

		return applyRepositoryMethodMetadata(em.createQuery(query));
	}

	/**
	 * Creates a new count query for the given {@link Specification}.
	 * 
	 * @param spec
	 *            can be {@literal null}.
	 * @return
	 */
	protected TypedQuery<Long> getCountQuery(Specification<T> spec) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> root = applySpecificationToCriteria(spec, query);

		if (query.isDistinct()) {
			query.select(builder.countDistinct(root));
		} else {
			query.select(builder.count(root));
		}

		return em.createQuery(query);
	}

	/**
	 * Applies the given {@link Specification} to the given
	 * {@link CriteriaQuery}.
	 * 
	 * @param spec
	 *            can be {@literal null}.
	 * @param query
	 *            must not be {@literal null}.
	 * @return
	 */
	private <S> Root<T> applySpecificationToCriteria(Specification<T> spec,
			CriteriaQuery<S> query) {

		Assert.notNull(query);
		Root<T> root = query.from(getDomainClass());

		if (spec == null) {
			return root;
		}

		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		}

		return root;
	}

	private TypedQuery<T> applyRepositoryMethodMetadata(TypedQuery<T> query) {

		if (crudMethodMetadata == null) {
			return query;
		}

		LockModeType type = crudMethodMetadata.getLockModeType();
		TypedQuery<T> toReturn = type == null ? query : query.setLockMode(type);

		for (Entry<String, Object> hint : crudMethodMetadata.getQueryHints()
				.entrySet()) {
			query.setHint(hint.getKey(), hint.getValue());
		}

		return toReturn;
	}

	/**
	 * Specification that gives access to the {@link Parameter} instance used to
	 * bind the ids for {@link SimpleJpaRepository#findAll(Iterable)}.
	 * Workaround for OpenJPA not binding collections to in-clauses correctly
	 * when using by-name binding.
	 * 
	 * @see https 
	 *      ://issues.apache.org/jira/browse/OPENJPA-2018?focusedCommentId=
	 *      13924055
	 * @author Oliver Gierke
	 */
	@SuppressWarnings("rawtypes")
	private final class ByIdsSpecification implements Specification<T> {

		ParameterExpression<Iterable> parameter;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.data.jpa.domain.Specification#toPredicate(javax
		 * .persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery,
		 * javax.persistence.criteria.CriteriaBuilder)
		 */
		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
				CriteriaBuilder cb) {

			Path<?> path = root.get(entityInformation.getIdAttribute());
			parameter = cb.parameter(Iterable.class);
			return path.in(parameter);
		}
	}
}