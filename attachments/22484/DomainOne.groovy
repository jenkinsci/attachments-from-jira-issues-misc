class DomainOne
{
	UUID id
	String name
	DomainTwo domaintwo
	
	static hasMany =
	{
		domainTwos: DomainTwo
	}
	
	static belongsTo =
	[
		DomainTwo
	]
	
	static mapping = 
	{
		id generator: 'uuid2', type: 'pg-uuid'
	}
	
	static mappedBy =
	[
		domainTwo: "domain_two_id"
	]	
}