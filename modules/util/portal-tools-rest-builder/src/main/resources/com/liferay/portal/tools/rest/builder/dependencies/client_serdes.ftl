package ${configYAML.apiPackagePath}.client.serdes.${escapedVersion};

<#list allSchemas?keys as schemaName>
	import ${configYAML.apiPackagePath}.client.dto.${escapedVersion}.${schemaName};
</#list>

import ${configYAML.apiPackagePath}.client.json.BaseJSONParser;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author ${configYAML.author}
 * @generated
 */
@Generated("")
public class ${schemaName}SerDes {

	public static ${schemaName} toDTO(String json) {
		${schemaName}JSONParser ${schemaVarName}JSONParser = new ${schemaName}JSONParser();

		return ${schemaVarName}JSONParser.parseToDTO(json);
	}

	public static ${schemaName}[] toDTOs(String json) {
		${schemaName}JSONParser ${schemaVarName}JSONParser = new ${schemaName}JSONParser();

		return ${schemaVarName}JSONParser.parseToDTOs(json);
	}

	public static String toJSON(${schemaName} ${schemaVarName}) {
		if (${schemaVarName} == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		<#assign
			enumSchemas = freeMarkerTool.getDTOEnumSchemas(openAPIYAML, schema)
			properties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, schema)
		/>

		<#list properties?keys as propertyName>
			<#assign propertyType = properties[propertyName] />

			<#if stringUtil.equals(propertyType, "Date") || stringUtil.equals(propertyType, "Date[]")>
				DateFormat liferayToJSONDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

				<#break>
			</#if>
		</#list>

		<#list properties?keys as propertyName>
			<#assign curName = propertyName?cap_first />

			<#if enumSchemas?keys?seq_contains(properties[propertyName])>
				<#assign curName = properties[propertyName] />
			</#if>

			if (${schemaVarName}.get${curName}() != null) {
				if (sb.length() > 1) {
					sb.append(", ");
				}

				sb.append("\"${propertyName}\": ");

				<#assign propertyType = properties[propertyName] />

				<#if allSchemas[propertyType]??>
					sb.append(String.valueOf(${schemaVarName}.get${propertyName?cap_first}()));
				<#else>
					<#if propertyType?contains("[]")>
						sb.append("[");

						for (int i = 0; i < ${schemaVarName}.get${propertyName?cap_first}().length; i++) {
							<#if stringUtil.equals(propertyType, "Date[]") || stringUtil.equals(propertyType, "Object[]") || stringUtil.equals(propertyType, "String[]") || enumSchemas?keys?seq_contains(propertyType)>
								sb.append("\"");

								<#if stringUtil.equals(propertyType, "Date[]")>
									sb.append(liferayToJSONDateFormat.format(${schemaVarName}.get${propertyName?cap_first}()[i]));
								<#elseif stringUtil.equals(propertyType, "Object[]") || stringUtil.equals(propertyType, "String[]")>
									sb.append(_escape(${schemaVarName}.get${propertyName?cap_first}()[i]));
								<#else>
									sb.append(${schemaVarName}.get${curName}()[i]);
								</#if>

								sb.append("\"");
							<#elseif stringUtil.startsWith(propertyType, "Map<")>
								sb.append(_toJSON(${schemaVarName}.get${propertyName?cap_first}()[i]));
							<#elseif allSchemas[propertyType?remove_ending("[]")]??>
								sb.append(String.valueOf(${schemaVarName}.get${propertyName?cap_first}()[i]));
							<#else>
								sb.append(${schemaVarName}.get${propertyName?cap_first}()[i]);
							</#if>

							if ((i + 1) < ${schemaVarName}.get${propertyName?cap_first}().length) {
								sb.append(", ");
							}
						}

						sb.append("]");
					<#else>
						<#if stringUtil.equals(propertyType, "Date") || stringUtil.equals(propertyType, "Object") || stringUtil.equals(propertyType, "String") || enumSchemas?keys?seq_contains(propertyType)>
							sb.append("\"");

							<#if stringUtil.equals(propertyType, "Date")>
								sb.append(liferayToJSONDateFormat.format(${schemaVarName}.get${propertyName?cap_first}()));
							<#elseif stringUtil.equals(propertyType, "Object") || stringUtil.equals(propertyType, "String")>
								sb.append(_escape(${schemaVarName}.get${propertyName?cap_first}()));
							<#else>
								sb.append(${schemaVarName}.get${curName}());
							</#if>

							sb.append("\"");
						<#elseif stringUtil.startsWith(propertyType, "Map<")>
							sb.append(_toJSON(${schemaVarName}.get${propertyName?cap_first}()));
						<#else>
							sb.append(${schemaVarName}.get${propertyName?cap_first}());
						</#if>
					</#if>
				</#if>
			}
		</#list>

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		${schemaName}JSONParser ${schemaVarName}JSONParser = new ${schemaName}JSONParser();

		return ${schemaVarName}JSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(${schemaName} ${schemaVarName}) {
		if (${schemaVarName} == null) {
			return null;
		}

		Map<String, String> map = new HashMap<>();

		<#assign
			enumSchemas = freeMarkerTool.getDTOEnumSchemas(openAPIYAML, schema)
			properties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, schema)
		/>

		<#list properties?keys as propertyName>
			<#assign propertyType = properties[propertyName] />

			<#if stringUtil.equals(propertyType, "Date") || stringUtil.equals(propertyType, "Date[]")>
				DateFormat liferayToJSONDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

				<#break>
			</#if>
		</#list>

		<#list properties?keys as propertyName>
			<#assign propertyType = properties[propertyName] />

			<#if enumSchemas?keys?seq_contains(propertyType)>
				if (${schemaVarName}.get${propertyType}() == null) {
					map.put("${propertyName}", null);
				}
				else {
					map.put("${propertyName}", String.valueOf(${schemaVarName}.get${propertyType}()));
				}
			<#elseif allSchemas[propertyType]??>
				if (${schemaVarName}.get${propertyName?cap_first}() == null) {
					map.put("${propertyName}", null);
				}
				else {
					map.put("${propertyName}", String.valueOf(${schemaVarName}.get${propertyName?cap_first}()));
				}
			<#elseif stringUtil.equals(propertyType, "Date")>
				map.put("${propertyName}", liferayToJSONDateFormat.format(${schemaVarName}.get${propertyName?cap_first}()));
			<#else>
				if (${schemaVarName}.get${propertyName?cap_first}() == null) {
					map.put("${propertyName}", null);
				}
				else {
					map.put("${propertyName}", String.valueOf(${schemaVarName}.get${propertyName?cap_first}()));
				}
			</#if>
		</#list>

		return map;
	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		return string.replaceAll("\"", "\\\\\"");
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\":");
			sb.append("\"");
			sb.append(entry.getValue());
			sb.append("\"");

			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static class ${schemaName}JSONParser extends BaseJSONParser<${schemaName}> {

		@Override
		protected ${schemaName} createDTO() {
			return new ${schemaName}();
		}

		@Override
		protected ${schemaName}[] createDTOArray(int size) {
			return new ${schemaName}[size];
		}

		@Override
		protected void setField(${schemaName} ${schemaVarName}, String jsonParserFieldName, Object jsonParserFieldValue) {
			<#list properties?keys as propertyName>
				<#if !propertyName?is_first>
					else
				</#if>

				if (Objects.equals(jsonParserFieldName, "${propertyName}")) {
					if (jsonParserFieldValue != null) {
						<#assign curName = propertyName?cap_first />

						<#if enumSchemas?keys?seq_contains(properties[propertyName])>
							<#assign curName = properties[propertyName] />
						</#if>

						${schemaVarName}.set${curName}(

						<#assign propertyType = properties[propertyName] />

						<#if stringUtil.equals(propertyType, "Date")>
							toDate((String)jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "Date[]")>
							toDates((Object[])jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "Integer")>
							Integer.valueOf((String)jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "Integer[]")>
							toIntegers((Object[])jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "Long")>
							Long.valueOf((String)jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "Long[]")>
							toLongs((Object[])jsonParserFieldValue)
						<#elseif stringUtil.startsWith(propertyType, "Map<")>
							${schemaName}SerDes.toMap((String)jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "Number")>
							Integer.valueOf((String)jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "Number[]")>
							toIntegers((Object[])jsonParserFieldValue)
						<#elseif stringUtil.equals(propertyType, "String[]")>
							toStrings((Object[])jsonParserFieldValue)
						<#elseif allSchemas?keys?seq_contains(propertyType)>
							${propertyType}SerDes.toDTO((String)jsonParserFieldValue)
						<#elseif propertyType?ends_with("[]") && allSchemas?keys?seq_contains(propertyType?remove_ending("[]"))>
							Stream.of(
								toStrings((Object[])jsonParserFieldValue)
							).map(
								object -> ${propertyType?remove_ending("[]")}SerDes.toDTO((String)object)
							).toArray(
								size -> new ${propertyType?remove_ending("[]")}[size]
							)
						<#elseif enumSchemas?keys?seq_contains(properties[propertyName])>
							${schemaName}.${propertyType}.create((String)jsonParserFieldValue)
						<#else>
							(${propertyType})jsonParserFieldValue
						</#if>

						);
					}
				}
			</#list>

			else {
				throw new IllegalArgumentException("Unsupported field name " + jsonParserFieldName);
			}
		}
	}

}