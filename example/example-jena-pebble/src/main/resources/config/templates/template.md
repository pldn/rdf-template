# Templating Example

## Concept example

{% for concept in concepts %}
### {{ concept._title | join(', ') }}

|                       |                                       |
|-----------------------|---------------------------------------|
| **preferred label**   | {{ concept.prefLabel  | join(', ') }} |
{% if concept.altLabel is not empty %}
| **alternative label** | {{ concept.altLabel   | join(', ') }} |
{% endif %}
| **definition**        | {{ concept.definition | join(', ') }} |
{% if concept.broaderConcept is not empty %}
| **broader concept**   | {% for item in concept.broaderConcept %}[{{ item | split("/") | last}}](#{{item | split("/") | last | lower}}){% if item != concept.broaderConcept | last %}, {% endif %}{% endfor %}|
{% endif %}

{% endfor %}

## Collection example

### {{ collection[0].label }}

| code                  | definition              |
|-----------------------|-------------------------|
{% for member in members %}
| {{ member.notation }} | {{ member.definition }} |
{% endfor %}
