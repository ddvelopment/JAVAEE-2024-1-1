//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.javaee.productdemoaop.mapper.manual;

import cn.edu.xmu.javaee.productdemoaop.dao.bo.OnSale;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.ProductPoSqlProvider;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.OnSalePo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPoExample;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductAllPo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductAllMapper {


    @Select({
            "SELECT p.id AS product_id, p.sku_sn, p.name, p.original_price, p.weight, p.barcode, p.unit, ",
            "p.origin_place, p.commission_ratio, p.free_threshold, p.status, ",
            "p.gmt_create, p.gmt_modified, ",

            "p.creator_id AS creator_id, p.creator_name AS creator_name, ",
            "p.modifier_id AS modifier_id, p.modifier_name AS modifier_name, ",

            // onSale fields
            "o.id AS onSale_id, o.product_id AS onSale_product_id, o.price AS onSale_price, ",
            "o.begin_time AS onSale_begin_time, o.end_time AS onSale_end_time, o.quantity AS onSale_quantity, ",
            "o.max_quantity AS onSale_max_quantity, ",

            // otherProduct fields
            "op.id AS otherProduct_id, op.goods_id AS otherProduct_goods_id, op.sku_sn AS otherProduct_skuSn, ",
            "op.name AS otherProduct_name, op.original_price AS otherProduct_originalPrice, ",
            "op.weight AS otherProduct_weight, op.barcode AS otherProduct_barcode, ",
            "op.unit AS otherProduct_unit, op.origin_place AS otherProduct_originPlace ",

            "FROM goods_product p ",
            "LEFT JOIN goods_onsale o ON p.id = o.product_id ",
            "LEFT JOIN goods_product op ON p.goods_id = op.goods_id ",
            "WHERE p.name = #{name}"
    })
    @ResultType(Map.class)
    List<Map<String, Object>> getProductWithAllByJoinRawData(String name);


    @SelectProvider(type=ProductPoSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
            @Result(column="commission_ratio", property="commissionRatio", jdbcType=JdbcType.INTEGER),
            @Result(column="free_threshold", property="freeThreshold", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.SMALLINT),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            @Result(property =  "onSaleList", javaType = List.class, many =@Many(select="selectLastOnSaleByProductId"), column = "id"),
            @Result(property =  "otherProduct", javaType = List.class, many =@Many(select="selectOtherProduct"), column = "goods_id")
    })
    List<ProductAllPo> getProductWithAll(ProductPoExample example);


    @Select({
            "select",
            "id, product_id, price, begin_time, end_time, quantity, max_quantity, creator_id, ",
            "creator_name, modifier_id, modifier_name, gmt_create, gmt_modified",
            "from goods_onsale",
            "where product_id = #{productId,jdbcType=BIGINT} and begin_time <= NOW() and end_time > NOW()"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
            @Result(column="price", property="price", jdbcType=JdbcType.BIGINT),
            @Result(column="begin_time", property="beginTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="quantity", property="quantity", jdbcType=JdbcType.INTEGER),
            @Result(column="max_quantity", property="maxQuantity", jdbcType=JdbcType.INTEGER),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    List<OnSalePo> selectLastOnSaleByProductId(Long productId);


    @Select({
            "select",
            "id, goods_id, sku_sn, name, original_price, weight, ",
            "barcode, unit, origin_place, creator_id, creator_name, modifier_id, ",
            "modifier_name, gmt_create, gmt_modified",
            "from goods_product",
            "where goods_id = #{goodsId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    ProductPo selectOtherProduct(Long goodsId);
}
