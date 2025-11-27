#!/usr/bin/env groovy
def call(args) {
	echo "Obteniendo fecha próxima liberación...\nArgumentos: ${args}"
	def fichero=args? args.fichero:null
	
	if(fichero==null) { 
		fichero = '/otroslogs/aplicaciones/weblogic/fechasLiberacion.txt'
		echo "Utilizando fichero por defecto ${fichero}"
	}
	
	def exists = fileExists fichero
	
	if(exists) { 
//		try { 
			echo "Leyendo fechas de liberación del fichero ${fichero}..."
			
			def contenidoFichero = readFile fichero
					
			def lineasFichero = contenidoFichero.split("\n")
			
			def fechasFicheroTexto = lineasFichero.findAll { it.contains('/') && !it.startsWith('#') }
			
			def fechasFichero = fechasFicheroTexto.collect { new java.text.SimpleDateFormat('dd/MM/yy').parse(it) }
			
			def hoy = new Date()
			
			echo "Fechas encontradas en el fichero:\n${fechasFichero}"
			
			/*
			def fechaMasCercana = Collections.min(fechasFichero, { d1,d2 ->
				echo "Comparando ${d1} con ${d2}..."
				def diff1 = Math.abs(d1.time - hoy.time)
				def diff2 = Math.abs(d2.time - hoy.time)
				
				def comp = Long.compare(diff1,diff2)
				
				echo "\tComp: ${comp}"
				
				return comp
			})
			*/
			def fechaMasCercana
			for(i = 0; i < fechasFichero.size(); i++) {
				def fecha = fechasFichero[i]
				
				/*JLP.- Las fechas pasadas las excluimos*/
				if(fecha.before(hoy)) {
					continue 
				}
				
				if(fechaMasCercana) { 
					def diff1 = Math.abs(fecha.time - hoy.time)
					def diff2 = Math.abs(fechaMasCercana.time - hoy.time)
					
					if(diff1<diff2) {
						fechaMasCercana = fecha 
					}
					
				} else {
					fechaMasCercana = fecha 
				}				 
			}
			
			echo "La fecha de liberación más cercana es ${fechaMasCercana}"
			 
			env.FECHA_PROXIMA_LIBERACION = fechaMasCercana ? fechaMasCercana.format('yy.MM.dd') : null
//		} catch (err) {
//			echo "Se produjo un error tratando de obtener la fecha de la próxima liberación: ${err}. Seguimos." 
//		}		
	} else {
		echo "No se encontró el fichero ${fichero}. No podemos obtener la fecha de la próxima liberación."
	}
}
