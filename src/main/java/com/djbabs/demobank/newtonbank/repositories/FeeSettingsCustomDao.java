package com.djbabs.demobank.newtonbank.repositories;

import com.chaka.chakaservice.model.FeeBandResponse;
import com.chaka.chakaservice.model.PagedResponse;
import com.chaka.chakaservice.model.Response;
import com.chaka.chakaservice.model.entities.fees.FeeBand;
import com.chaka.chakaservice.model.entities.fees.FeesSettings;
import com.chaka.chakaservice.utils.DbConstants;
import com.chaka.chakaservice.utils.RowCountMapper;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class FeeSettingsCustomDao {

    private SimpleJdbcCall getFeesById, getFeeByName, insertUpdateFee, deleteFeeById,
    getFeeByMerchant, getAllFees, getUserFeeBand, deleteFeeBand;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        getFeeByName = new SimpleJdbcCall(jdbcTemplate).withProcedureName("fee_getbyname")
                .returningResultSet(DbConstants.SINGLE_RESULT, BeanPropertyRowMapper.newInstance(FeesSettings.class));
        getUserFeeBand = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_fee_percentage_by_userId")
                .returningResultSet(DbConstants.SINGLE_RESULT, BeanPropertyRowMapper.newInstance(FeeBandResponse.class));

        deleteFeeById = new SimpleJdbcCall(jdbcTemplate).withProcedureName("delete_fee_byid")
                .returningResultSet(DbConstants.SINGLE_RESULT, BeanPropertyRowMapper.newInstance(FeesSettings.class));

        deleteFeeBand = new SimpleJdbcCall(jdbcTemplate).withProcedureName("delete_fee_band")
                .returningResultSet(DbConstants.SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Object.class));
        
        getFeesById = new SimpleJdbcCall(jdbcTemplate).withProcedureName("fee_getbyid")
                .returningResultSet(DbConstants.SINGLE_RESULT, BeanPropertyRowMapper.newInstance(FeesSettings.class));

        getAllFees = new SimpleJdbcCall(jdbcTemplate).withProcedureName("fee_findall")
                .returningResultSet(DbConstants.RESULT_COUNT, new RowCountMapper())
                .returningResultSet(DbConstants.MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(FeesSettings.class));

        insertUpdateFee = new SimpleJdbcCall(jdbcTemplate).withProcedureName("fee_insertupdate").withReturnValue()
                .returningResultSet(DbConstants.MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(FeesSettings.class));
        
        
    }

    public PagedResponse<FeesSettings> findAll(int pageNumber, int pageSize, String searchByName,String merchantId) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("pageNumber", pageNumber)
                .addValue("pageSize", pageSize)
                .addValue("searchByName", TextUtils.isEmpty(searchByName)? null: searchByName)
                .addValue("merchantId", TextUtils.isEmpty(merchantId)? null: merchantId);


        Map<String,Object> m = getAllFees.execute(in);
        Integer count = ((ArrayList<Integer>) m.get(DbConstants.RESULT_COUNT)).get(0);
        List<Object> content = (ArrayList<Object>) m.getOrDefault(DbConstants.MULTIPLE_RESULT, null);

        return new PagedResponse(pageNumber, pageSize, count, content);
    }

    public <S extends FeesSettings> S save(S model) {
        /*SqlParameterSource in = new BeanPropertySqlParameterSource(model);*/

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("id", model.getId())
                .addValue("name", model.getName())
                .addValue("value",model.getValue())
                .addValue("IsPercentage", model.getIsPercentage())
                .addValue("description", model.getDescription())
                .addValue("merchantId", model.getMerchantId())
                // Audit Properties
                .addValue("dateCreated", model.getDateCreated())
                .addValue("dateModified", model.getDateModified())
                .addValue("modifiedBy", model.getModifiedBy())
                .addValue("createdBy", model.getCreatedBy());

        Map<String,Object> m = insertUpdateFee.execute(in);

        int resultCode =  m.get(DbConstants.RETURN_VALUE) != null? (int)m.get(DbConstants.RETURN_VALUE): 0;
        model.setId(Long.valueOf(resultCode));

        return model;
    }

    public Response<FeesSettings> findById(long id, int merchantId) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("merchantId", merchantId);

        Map<String,Object> m = getFeesById.execute(in);
        List<FeesSettings> content = (ArrayList<FeesSettings>) m.get(DbConstants.SINGLE_RESULT);

        return new Response(content.isEmpty()? null: content.get(0));
    }
    @Transactional
    public Response<FeesSettings> findByName(String name, String merchantId) {

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("merchantId", merchantId);

        Map<String,Object> m = getFeeByName.execute(in);
        List<FeesSettings> content = (ArrayList<FeesSettings>) m.get(DbConstants.SINGLE_RESULT);

        return new Response(content.isEmpty()? null: content.get(0));
    }
    @Transactional
    public PagedResponse<FeesSettings> findByMerchant(String merchantId, @Nullable int pageNum,@Nullable int pageSize) {

        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("merchantId", merchantId)
                .addValue("pageNum", pageNum)
                .addValue("pageSize", merchantId);

        Map<String,Object> m = getFeeByMerchant.execute(in);
        List<FeesSettings> content = (ArrayList<FeesSettings>) m.get(DbConstants.MULTIPLE_RESULT);
        Integer count = m.get ("cnt")!=null? (Integer)m.get("cnt"): 0;

        return new PagedResponse(pageNum, pageSize, count, content);
    }

    @Transactional
    public void deleteById(Long id, int merchantId) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("merchantId", merchantId);

        Map<String,Object> m = deleteFeeById.execute(in);
    }

    @Transactional
    public void deleteByFeeBandId(Long id) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("id", id);

        deleteFeeBand.execute(in);
    }
    
    public void deleteAll(Iterable<? extends FeesSettings> iterable) {

    }
    
    @SuppressWarnings("unchecked")
    public Response<FeeBandResponse> getFeeBandByUserId(String userId, int merchantId, String currency, Double currentOrderAmount) {

    	Calendar dateInst = Calendar.getInstance();
    	Date today = dateInst.getTime();
    	dateInst.add(Calendar.DATE, -30);

    	Date last30Days = dateInst.getTime();
    	
    	MapSqlParameterSource in = new MapSqlParameterSource()
    			.addValue("userId", userId)
    			.addValue("merchantId", merchantId)
    			.addValue("currency", currency)
    			.addValue("_today", today)
    			.addValue("last_30Days", last30Days)
    			.addValue("current_order", currentOrderAmount == null ? 0 : currentOrderAmount);




    	Map<String,Object> m = getUserFeeBand.execute(in);
    	List<FeeBand> content = (ArrayList<FeeBand>) m.get(DbConstants.SINGLE_RESULT);

    	return new Response(content.isEmpty()? null: content.get(0));
    }

}
