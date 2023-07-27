package com.example.one_vote_service.utils;

import com.example.one_vote_service.exception.DaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
public class StoreProcedureCaller extends SimpleJdbcCall {
    private String requestId;
    private final MapSqlParameterSource mapSqlParameterSource;

    public StoreProcedureCaller(DataSource dataSource, String requestId, String catalog, String procedureName) {
        super(dataSource);
        withRequestId(requestId);
        withCatalogName(catalog);
        withProcedureName(procedureName);
        this.mapSqlParameterSource = new MapSqlParameterSource();
    }

    public StoreProcedureCaller(DataSource dataSource) {
        super(dataSource);
        this.mapSqlParameterSource = new MapSqlParameterSource();
    }

    public StoreProcedureCaller withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    @Override
    public StoreProcedureCaller withCatalogName(String catalogName) {
        super.withCatalogName(catalogName);
        return this;
    }

    @Override
    public StoreProcedureCaller withSchemaName(String schemaName) {
        super.withSchemaName(schemaName);
        return this;
    }

    @Override
    public StoreProcedureCaller withProcedureName(String procedureName) {
        super.withProcedureName(procedureName);
        return this;
    }

    @Override
    public StoreProcedureCaller returningResultSet(String parameterName, RowMapper<?> rowMapper) {
        super.returningResultSet(parameterName, rowMapper);
        return this;
    }

    public StoreProcedureCaller returningResultSetByIndex(int index, RowMapper<?> rowMapper) {
        super.returningResultSet("#result-set-" + index, rowMapper);
        return this;
    }

    @Override
    public StoreProcedureCaller declareParameters(SqlParameter... sqlParameters) {
        for (SqlParameter sqlParameter : sqlParameters) {
            if (sqlParameter != null) {
                this.addDeclaredParameter(sqlParameter);
                log.debug(String.format("requestId: %s, [OUT PARAM] %s = %s", this.requestId, sqlParameter.getName(), sqlParameter.getSqlType()));
            }
        }
        return this;
    }

    public StoreProcedureCaller register(String param, int sqlType) {
        log.debug(String.format("requestId: %s, [OUT PARAM] %s = %s", this.requestId, param, sqlType));
        this.addDeclaredParameter(new SqlInOutParameter(param, sqlType));
        return this;
    }

    public StoreProcedureCaller setValue(String param, Object value) {
        log.debug(String.format("requestId:%s, [IN PARAM] %s = %s", this.requestId, param, value));
        this.mapSqlParameterSource.addValue(param, value);
        return this;
    }

    public Map<String, Object> execute() {
        long begin = System.currentTimeMillis();
        Map<String, Object> result = this.doExecute(this.mapSqlParameterSource);
        log.debug(String.format("requestId: %s, completed calling [%s] in %s ms", requestId, this.getCallString(), System.currentTimeMillis() - begin));
        return result;
    }

    public Map<String, Object> executeOnError(String paramCode, String paramDetail) {
        Map<String, Object> result = this.execute();
        int code = (int) result.get(paramCode);
        String message = (String) result.get(paramDetail);
        if (code != 0) throw new DaoException(requestId, code, message);
        return result;
    }

    public Map<String, Object> executeOnError() {
        return this.executeOnError("P_CODE", "P_DETAIL");
    }
}
