package info.ivicel.springflasky.web.repository.base;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private JpaEntityInformation<T, ?> entityInformation;
    private EntityManager em;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.entityInformation = entityInformation;
        this.em = em;
    }


    @Override
    public Page<T> findAllJoin(Specification<T> spec, Pageable pageable) {
        Class<T> domainClass = entityInformation.getJavaType();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery query = cb.createQuery();
        Root root = query.from(domainClass);
        query.select(root);
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            if (predicate != null) {
                query.where(predicate);
            }
        }


        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        if (sort.isSorted()) {
            query.orderBy(toOrders(sort, root, cb));
        }

        TypedQuery<T> typedQuery = em.createQuery(query);

        return pageable.isUnpaged() ? new PageImpl<>(typedQuery.getResultList())
                : readPage(typedQuery, domainClass, pageable, spec);
    }
}
