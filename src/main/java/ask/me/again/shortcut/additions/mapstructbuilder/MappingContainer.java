package ask.me.again.shortcut.additions.mapstructbuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class MappingContainer {

  private final
  List<String> mappings;
  private final
  Set<String> inputObjects;

  MappingContainer(List<String> mappings, Set<String> inputObjects) {
    this.mappings = mappings;
    this.inputObjects = inputObjects;
  }

  public static MappingContainerBuilder builder() {
    return new MappingContainerBuilder();
  }

  public List<String> getMappings() {
    return this.mappings;
  }

  public Set<String> getInputObjects() {
    return this.inputObjects;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof MappingContainer)) return false;
    final MappingContainer other = (MappingContainer) o;
    final Object this$mappings = this.getMappings();
    final Object other$mappings = other.getMappings();
    if (this$mappings == null ? other$mappings != null : !this$mappings.equals(other$mappings)) return false;
    final Object this$inputObjects = this.getInputObjects();
    final Object other$inputObjects = other.getInputObjects();
    if (this$inputObjects == null ? other$inputObjects != null : !this$inputObjects.equals(other$inputObjects))
      return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $mappings = this.getMappings();
    result = result * PRIME + ($mappings == null ? 43 : $mappings.hashCode());
    final Object $inputObjects = this.getInputObjects();
    result = result * PRIME + ($inputObjects == null ? 43 : $inputObjects.hashCode());
    return result;
  }

  public String toString() {
    return "MappingContainer(mappings=" + this.getMappings() + ", inputObjects=" + this.getInputObjects() + ")";
  }

  public static class MappingContainerBuilder {
    private ArrayList<String> mappings;
    private ArrayList<String> inputObjects;

    MappingContainerBuilder() {
    }

    public MappingContainerBuilder mapping(String mapping) {
      if (this.mappings == null) this.mappings = new ArrayList<String>();
      this.mappings.add(mapping);
      return this;
    }

    public MappingContainerBuilder mappings(Collection<? extends String> mappings) {
      if (this.mappings == null) this.mappings = new ArrayList<String>();
      this.mappings.addAll(mappings);
      return this;
    }

    public MappingContainerBuilder clearMappings() {
      if (this.mappings != null)
        this.mappings.clear();
      return this;
    }

    public MappingContainerBuilder inputObject(String inputObject) {
      if (this.inputObjects == null) this.inputObjects = new ArrayList<String>();
      this.inputObjects.add(inputObject);
      return this;
    }

    public MappingContainerBuilder inputObjects(Collection<? extends String> inputObjects) {
      if (this.inputObjects == null) this.inputObjects = new ArrayList<String>();
      this.inputObjects.addAll(inputObjects);
      return this;
    }

    public MappingContainerBuilder clearInputObjects() {
      if (this.inputObjects != null)
        this.inputObjects.clear();
      return this;
    }

    public MappingContainer build() {
      List<String> mappings;
      switch (this.mappings == null ? 0 : this.mappings.size()) {
        case 0:
          mappings = java.util.Collections.emptyList();
          break;
        case 1:
          mappings = java.util.Collections.singletonList(this.mappings.get(0));
          break;
        default:
          mappings = java.util.Collections.unmodifiableList(new ArrayList<String>(this.mappings));
      }
      Set<String> inputObjects;
      switch (this.inputObjects == null ? 0 : this.inputObjects.size()) {
        case 0:
          inputObjects = java.util.Collections.emptySet();
          break;
        case 1:
          inputObjects = java.util.Collections.singleton(this.inputObjects.get(0));
          break;
        default:
          inputObjects = new java.util.LinkedHashSet<String>(this.inputObjects.size() < 1073741824 ? 1 + this.inputObjects.size() + (this.inputObjects.size() - 3) / 3 : Integer.MAX_VALUE);
          inputObjects.addAll(this.inputObjects);
          inputObjects = java.util.Collections.unmodifiableSet(inputObjects);
      }

      return new MappingContainer(mappings, inputObjects);
    }

    public String toString() {
      return "MappingContainer.MappingContainerBuilder(mappings=" + this.mappings + ", inputObjects=" + this.inputObjects + ")";
    }
  }
}
