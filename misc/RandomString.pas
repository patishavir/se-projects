program RandomString;
function generateRandomString () : string;

const
  stringLength = 16;
var
  i: Byte;
  s: string;

  begin
    s := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    s := s + 'abcdefghijklmnopqrstuvwxyz';
    s := s + '0123456789';

  generateRandomString := '';

  for i := 0 to stringLength-1 do
    generateRandomString := generateRandomString + s[Random(Length(s))+1];
 { writeln(generateRandomString); }
end;

var i: Integer;

begin (* Main *)
  Randomize;
  writeln(RandSeed);
  For i:=1 to 100 do
        writeln (generateRandomString());
  readln()
end. (* Main *)
