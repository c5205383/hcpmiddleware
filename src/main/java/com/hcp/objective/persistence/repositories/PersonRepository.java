package com.hcp.objective.persistence.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.Person;
import com.hcp.objective.persistence.bean.Person_;



@Repository
@Transactional
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Lazy(true)
public class PersonRepository extends GeneralJpaRepository<Person, Long> {

	/*
	 * Constructors
	 */

	public PersonRepository() {
		super(Person.class);
	}

	/*
	 * Actions
	 */

	@Transactional(readOnly = true)
	public List<Person> findByFirstName(final String firstName, Sort sort) {
		List<Person> models = this.findAll(
				generateSearchQueryPredicate(firstName), sort);
		return models;
	}

	/*
	 * Private & Protected Methods
	 */

	private Specification<Person> generateSearchQueryPredicate(
			final String firstName) {
		return new Specification<Person>() {
			@Override
			public Predicate toPredicate(Root<Person> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				ArrayList<Predicate> predicates = new ArrayList<Predicate>(5);

				if (!StringUtils.isEmpty(firstName)) {
					predicates.add(cb.like(root.get(Person_.name), "%"
							+ firstName + "%"));
				}
				return predicates.isEmpty() ? null
						: predicates.size() == 1 ? predicates.get(0) : cb
								.and(predicates
										.toArray(new Predicate[predicates
												.size()]));
			}
		};
	}
}
