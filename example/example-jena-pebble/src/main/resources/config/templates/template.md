# Templating Example

## Concept example

{% for concept in concepts %}
### {{ concept.prefLabel }}

|                  |                          |
|------------------|--------------------------|
| **voorkeurterm** | {{ concept.prefLabel }}  |
| **definitie**    | {{ concept.definition }} |

{% endfor %}

## Collection example

### {{ collection.label }}

| code                  | definition              |
|-----------------------|-------------------------|
{% for member in members %}
| {{ member.notation }} | {{ member.definition }} |
{% endfor %}
