package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NamedParameterJdbcTemplate
 * : 이름을 지정해서 파라미터 바인딩 하는 기능 제공
 * Map
 * SqlParameterSource
 *  - MapSqlParameterSource : sql에 좀 더 특화된 기능 제공
 *  - BeanPropertyParameterSource : 자바빈 프로퍼티 규약을 통해 자동으로 파라미터 객체 생성
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    private final NamedParameterJdbcTemplate template;

    /**
     * dataSource를 의존관계 주입 받고 생성자 내부에서 jdbcTemplate를 생성한다.
     * @param dataSource
     */
    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * 데이터 저장
     * ? 대신 :파라미터 이름 사용
     * @param item
     * @return
     */
    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (:itemName, :price, :quantity)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // pk id 값을 db가 생성하기 때문에 데이터 저장전까지 id를 모른다.. => id를 알기 위해서 keyHolder 사용
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    /**
     * 데이터 업데이트
     * @param itemId
     * @param updateParam
     */
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);
        template.update(sql, param);
    }

    /**
     * 데이터 하나 조회
     * queryForObject() : 결과 로우가 하나일 때 사용
     * @param id
     * @return
     */
    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id =:id";

        try {
            Map<String, Object> param = Map.of("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) { // 결과가 없으면 EmptyResultDataAccessException 예외 발생
            return Optional.empty(); // 결과가 없으면 예외를 잡아서 Optional.empty() 값 반환
        }
    }


    /**
     * 데이터 리스트 조회
     * query() : 결과가 하나 이상일 때 사용
     * @param cond
     * @return
     */
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name, price, quantity from item";

        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%', :itemName , '%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper());
    }

    /**
     * RowMapper는 데이터베이스의 반환 결과인 ResultSet을 객체로 변환한다.
     * 데이터베이스 조회 결과를 객체로 변환할 때 사용
     * 결과가 없으면 빈 컬렉션을 반환한다.
     * @return
     */
    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); // camel 변환 지원

    }
}
