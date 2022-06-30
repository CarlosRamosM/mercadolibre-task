package mx.com.ml.rebell.alliance.message.dto.base;

import lombok.NoArgsConstructor;
import mx.com.ml.rebell.alliance.message.util.JsonUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONObject;

import java.io.Serializable;

@NoArgsConstructor
public abstract class BaseDto implements Serializable {

  private static final long serialVersionUID = 1L;

  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public boolean equals(final Object obj) {
    return obj != null && this.getClass() == obj.getClass() && EqualsBuilder.reflectionEquals(this, obj, false);
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

  public JSONObject asJson() {
    return JsonUtil.asJsonString(this).map(JSONObject::new).orElseThrow(IllegalStateException::new);
  }
}
