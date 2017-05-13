/*$("id") select dom
There is not jQuery
Using $("id").click wont work.
	The page sends a JSON with the data of the click.
	One of those being sended is a "isTrusted" and it will be false if done by code.
	
Other strategy: just color the places.
	Will I be fast enough for that?
	
We will use $("id").backgroundColor = cyan to mark the words

The strategy: use brute force to search possible first letter,
then look the vicinity for the second letter, and then search in that direction.
We will mark the boxes so we will just have to click them.

VERSION 2

I will definitely make a postmortem of the event with lessons learnt from it.

AFTER NINE HOURS LOST IN THE SEARCH OF FAKING EVENTS WITH ISTRUE.
I decided that there should be some other way and started doing (bad) things to chrome. After profiling,
checking every VM one by one (although I already got the code at the end of the script, but i couldn't understand a bit)
and getting (by sheer luck) the VM with the web socket, when receiving the message
(So i went to learn how they work and understanding that send sends something to it)
I could finally scrape what triggers the solution. Yey (Also learnt how lambda functions work in JS)

So in basis:

Get a word of the list of words.
Look for first letter
	Look for the next letter in the vicinity
		Look if it matches until the end
			Get the first and last node, make the message, hash, and send to websocket
Repeat
*/

//START OF CODE

//This is used for hashing the message to websocket.
 const m = t=>{
            let n, e, o, s = 0;
            const c = t + "-saltbae";
            if (!c.length)
                return s;
            for (n = 0,
            o = c.length; n < o; n++)
                e = c.charCodeAt(n),
                s = (s << 5) - s + e,
                s |= 0;
            return Math.abs(s)
        }

//MAIN FUNCTIONS

function search(){
	
	//everythingToWhite();
	
    var nodeWords = $("words");
    for(var numberWord=1; numberWord<nodeWords.childElementCount-2; numberWord++){
    searchFirstLetter(nodeWords.childNodes[numberWord].innerHTML, getRandomColor());
	}
}

function everythingToWhite(){
	for(var row =0; row<30; row++){
		for (var col=0; col<30; col++){
			
			colourWhite(row, col);
            
        }
    }
}

function searchFirstLetter (str, color){
	var firstLetter=str.charAt(0);
	
    for(var row =0; row<30; row++){
		for (var col=0; col<30; col++){
			
			if(getLetter(row,col) == firstLetter){
				if(searchSecondLetter(str, row, col, color)){
					//Legacy = we used to color letter for a first version
					return true;
				}
			}
            
        }
    }
}

function searchSecondLetter(str, row, col, color){
    var letter = str.charAt(1);

	for(var rowDiff =-1; rowDiff <=1; rowDiff++){
        for(var colDiff=-1; colDiff<=1; colDiff++){
            if(rowDiff!=0 || colDiff!=0){
				if(getLetter(row+rowDiff,col+colDiff) == letter){
					if(searchForLastLetter(str, row, col, rowDiff, colDiff, color)){
						
						return true;
					}
				}
            }
        }
    }
	return false;
}

function searchForLastLetter(str, row, col, rowDiff, colDiff, color){
	//Initialize at the second letter.
	var index=1;
	var nextRow = row + rowDiff;
	var nextCol = col + colDiff;
	
	//We iterate to see if the next letter are the same.
	do{
		index++;
		nextRow += rowDiff;
		nextCol += colDiff;
	}while(str.charAt(index) == getLetter(nextRow, nextCol));
	
	if(index== str.length){ //All the text was equal
		nextRow -= rowDiff;
		nextCol -= colDiff;
		
		solve(row,col,nextRow,nextCol);

		return true;
	}else{
		return false;
	}
}


//HELPER FUNCTIONS
function getRandomColor(){
	return '#'+Math.random().toString(16).substr(-6);
}

//The colouring was used for testing and to try to solve the challenge by handleEvent
//It was truly desperation.
function colourWhite(row, col){

	var node = $(id(row,col));
	if(node!=null){
		node.style.color = "white";
	}
}

function colourStart(row, col, color){
	if($(id(row,col)).style.color=="white"){
		$(id(row,col)).style.color="black";
	}
	$(id(row,col)).style.fontWeight = "bold";
	$(id(row,col)).style.backgroundColor = color;
}


function colourMiddle(row,col, color){
	$(id(row,col)).style.color = color;
	$(id(row,col)).style.fontWeight = "bold";
}

function colourEnd(row, col, color){
	$(id(row,col)).style.border = "5px solid "+color;
	$(id(row,col)).style.fontWeight = "bold";
}

function getLetter(row, col){
	var soupLetter=$(id(row,col));
	if(soupLetter!=null){
		return soupLetter.innerHTML;
	}else{
		return null;
	}
}

function id(row,col){
	return row+"-"+col;
}

//Once we find the WebSocket between the chrome VM, the problem is easily solved.
function solve(firstRow, firstCol, lastRow, lastCol){
	e=getText(firstCol,firstRow,lastCol,lastRow); 
	w.send(btoa(`${e}-${m(e)}`))
}

function getText(firstRow, firstCol, finalRow, finalCol){
	return ""+firstCol+"-"+firstRow+"-"+finalCol+"-"+finalRow;
}

		
/*
(function() {
    {
        const t = d.body
          , n = 6
          , e = 6
          , o = 1
          , s = [["H", "Y", "L", "D", "T", "C"], ["W", "H", "D", "R", "P", "J"], ["I", "Y", "A", "F", "C", "Z"], ["G", "F", "A", "W", "E", "D"], ["Y", "L", "F", "T", "T", "O"], ["T", "B", "E", "G", "I", "B"]]
          , c = ["TBEG", "OEFDY", "TPCET", "WYATI", "WIGYT"];
        let r = 300;
        w.onclose = (()=>setTimeout(()=>l.reload(), 1e3));
        let a = "";
        for (let t = 0; t < e; t++) {
            a += "<tr>";
            for (let e = 0; e < n; e++)
                a += `<td id="${e}-${t}">${s[t][e]}</td>`;
            a += "</tr>"
        }
        const i = `<div id="container">` + `<table style="width:${40 * n}px; height:${40 * e}px"><tbody>${a}</tbody></table>` + `<div id="words">` + `<h1>Level ${o}</h1>` + c.map(t=>`<div id="word-${t}">${t}</div>`).join("") + `<div id="time-left">` + `Time left: <span id="time${o}">${r}</span>` + `</div>` + `<div>` + `</div>`;
        let f = null;
        const u = t=>{
            const n = $(t).getBoundingClientRect();
            const e = n.left + (n.right - n.left >> 1);
            const o = n.top + (n.bottom - n.top >> 1);
            return [e, o]
        }
        ;
        d.selectWord = ((n,e,o)=>{
            const [s,c] = n.split("-").map(Number);
            const [r,a] = e.split("-").map(Number);
            const i = Math.abs(s - r) == Math.abs(c - a);
            const [l,d] = u(n);
            const [f,m] = u(e);
            const b = Math.abs(l - f);
            const h = Math.abs(d - m);
            const M = $(`word-${o}`);
            if (!M)
                return;
            M.className = "word-done";
            let L = 0;
            let g = 0;
            i ? (L = s < r ? c < a ? 45 : -45 : c < a ? 135 : 225,
            g = 0 | Math.sqrt(b * b + h * h)) : (L = s === r ? c < a ? 90 : -90 : s < r ? 0 : 180,
            g = 0 | Math.max(b, h));
            t.innerHTML += `<div style="position:absolute; top:${d - 16 - 2}px; left:${l - 16 - 2}px; pointer-events:none; ; transform:rotate(${L}deg); transform-origin:18px 18px">` + `<svg width="${g + 32 + 4}" height="36" xmlns="http://www.w3.org/2000/svg">` + `<rect stroke="#55f" stroke-width="3" fill-opacity="0.2" fill="#55f" x="2" y="2" width="${g + 32}" rx="16" ry="16" height="32"/>` + `</svg>` + `</div>`
        }
        );
        const m = t=>{
            let n, e, o, s = 0;
            const c = t + "-saltbae";
            if (!c.length)
                return s;
            for (n = 0,
            o = c.length; n < o; n++)
                e = c.charCodeAt(n),
                s = (s << 5) - s + e,
                s |= 0;
            return Math.abs(s)
        }
          , b = t=>{
            if (!t.screenX || !t.screenY || !t.isTrusted)
                return;
            const n = t.target;
            if ("TD" === n.nodeName)
                if (f) {
                    $(f).classList.remove("cell-selected");
                    const t = n.id
                      , e = `${f}-${t}`;
                    w.send(btoa(`${e}-${m(e)}`)),
                    f = ""
                } else
                    f = n.id,
                    n.classList.add("cell-selected")
        }
        ;
        t.innerHTML = i,
        t.addEventListener("click", b);
        let h = setInterval(()=>{
            const t = $(`time${o}`);
            t ? (r--,
            r < 11 && (t.className = "big-time"),
            t.innerHTML = Math.max(0, r)) : clearInterval(h)
        }
        , 1e3);
        window[String.fromCharCode(36)] = d.getElementById.bind(d)
    }
}
)

w = new WebSocket(`ws://${h}:3636${p}`);
w.onmessage = m=>f(a(m.data))()

w.send(btoa(`${e}-${m(e)}`)),
e="3-4-0-4" -> "Principio-final"

const m = t=>{   //t="3-4-0-4
	let n, e, o, s = 0;
	const c = t + "-saltbae";
	if (!c.length)
		return s;
	for (n = 0,
	o = c.length; n < o; n++)
		e = c.charCodeAt(n),
		s = (s << 5) - s + e,
		s |= 0;
	return Math.abs(s)

*/

/*
 w.send(btoa(`${e}-${m(e)}`)) //Con cada combinacion +
 
 const m = t=>{
            let n, e, o, s = 0;
            const c = t + "-saltbae";
            if (!c.length)
                return s;
            for (n = 0,
            o = c.length; n < o; n++)
                e = c.charCodeAt(n),
                s = (s << 5) - s + e,
                s |= 0;
            return Math.abs(s)
        }
		//Para hashearlo
		
function sacarTexto(firstRow, firstCol, finalRow, finalCol){
	return ""+firstCol+"-"+firstRow+"-"+finalCol+"-"+finalRow;
}
*/