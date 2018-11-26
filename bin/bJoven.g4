grammar bJoven; //must match filename.g

expression: NOTE + END;

NOTE : DURATION + TIMBRE + VOLUME? ;

DURATION : 'whole' | 'half' | 'quart' | 'eight' | 'sixh' ;

VOLUME : [0-100];

TIMBRE : OCTAVE + BASICNOTE + MODE?;

OCTAVE: [1-10];

BASICNOTE : 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'X' ; // X - dead note, a note that is played very silently.

MODE : DIESE | DOUBLE_DIESE | BEMOL | DOUBLE_BEMOL | BECARRE;

DIESE : '+' | '#' ;

DOUBLE_DIESE : '++' | '##' ;

BEMOL : '-' | 'b' ;

DOUBLE_BEMOL : '--' | 'bb' ;

BECARRE : '*' ;

END : '.';