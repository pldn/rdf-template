# Templating Example

## Concept example

{% for concept in concepts %}
### {{ concept.prefLabel }}

|                       |                                       |
|-----------------------|---------------------------------------|
| **voorkeurterm**      | {{ concept.prefLabel  | join(', ') }} |
| **alternatieve term** | {{ concept.altLabel   | join(', ') }} |
| **definitie**         | {{ concept.definition | join(', ') }} |

{% endfor %}

## Collection example

### {{ collection[0].label }}

| code                  | definition              |
|-----------------------|-------------------------|
{% for member in members %}
| {{ member.notation }} | {{ member.definition }} |
{% endfor %}
