count = 200
speed = 0.5
delay = 0.05
duration = 10.800

lastSpeed = 0
totalDelay = 0.0
numberOfStripes = nil

sprite = Array.new(count){|i|
    "Sprite R 0.0 S#{i.to_s.rjust(3,'0')} 100.0 OUGI_OUKA_MUSOUGEKI_RAY14"
}
position = Array.new(count){|i|
    "Position R 0.0 S#{i.to_s.rjust(3,'0')} -8 #{Random.rand(-4.5..4.5)}"
}
move = Array.new(count){|i|
    d = Random.rand((0.5*delay)..(2*delay))
    s = Random.rand((0.8*speed)..(1.2*speed))
    lastSpeed = s
    totalDelay += d
    if totalDelay + s - d > duration && numberOfStripes == nil
          numberOfStripes = i+1
    end
    "Move R #{d} S#{i.to_s.rjust(3,'0')} Line #{s} R 16 #{Random.rand((-0.2)..(-0.1))}"    
}
show = Array.new(count){|i|
    "Show R 0.0 S#{i.to_s.rjust(3,'0')} #{lastSpeed+totalDelay}"
}

puts sprite[0,numberOfStripes].join("\n")
puts ""
puts position[0,numberOfStripes].join("\n")
puts ""
puts move[0,numberOfStripes].join("\n")
puts ""
puts show[0,numberOfStripes].join("\n")