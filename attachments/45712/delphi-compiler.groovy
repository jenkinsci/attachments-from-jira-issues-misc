// Custom Groovy-Parser für das Jenkins warnings-ng Plugin
//
// Einstellungen für Jenkins:
// Parser Name: Delphi Compiler
// Parser ID  : delphi-compiler
// RegEx      : (.*)\((\d+)\).*((H|W|E|F)\d+)\s*(.*)
// Groovy Skript:

import edu.hm.hafner.analysis.Severity

String fileName = matcher.group(1)
String lineNumber = Integer.parseInt(matcher.group(2))
String category = matcher.group(3)
String type = matcher.group(4)
String message = matcher.group(5)
Severity severity = Severity.WARNING_NORMAL

switch ( category ) {
    case "W1010": // Methode '###' verbirgt virtuelle Methode vom Basistyp '###'
    case "W1018": // Case label outside of range of case expression
    case "W1020": // Instanz von '###' mit der abstrakten Methode '###' wird angelegt
    case "W1022": // Comparison always evaluates to True
    case "W1035": // Rückgabewert der Funktion '###' könnte undefiniert sein
    case "W1036": // Variable '###' ist möglicherweise nicht initialisiert worden
    case "W1058": // Implicit string cast with potential data loss from '###' to '###'
        severity = Severity.WARNING_HIGH
        break

    case "H1054": // Benutzerdefinierter Hinweis
    case "H2219": // Das private-Symbol '###' wurde deklariert, aber nie verwendet
    case "H2443": // Inline-Funktion '###' wurde nicht expandiert, weil Unit '###' in der USES-Liste nicht angegeben ist
    case "W1023": // Vorzeichenbehaftete und -lose Typen werden verglichen - beide Operanden werden erweitert
    case "W1024": // Vorzeichenbehaftete und -lose Typen werden kombiniert - beide Operanden werden erweitert
        severity = Severity.WARNING_LOW
        break

    default:
        Severity.WARNING_NORMAL
}

switch ( type ) {
    case "E" : 
        type = "Error"
        severity = Severity.ERROR
        break
    case "F" : 
        type = "Failure"
        severity = Severity.ERROR
        break
    case "H" :
        type = "Hint"
        break
    case "W" : 
        type = "Warning"
        break
}

builder.setFileName(fileName)
       .setLineStart(lineNumber)
       .setSeverity(severity)
       .setCategory(category)
       .setType(type)
       .setMessage(message)

return builder.buildOptional();