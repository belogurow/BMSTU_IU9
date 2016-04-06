(define (div x y)
  (remainder x y))
(define (mod x y)
  (quotient x y))

(define (day-of-week dd mm yyyy)
  (let* ((a (mod (- 14 mm) 12))
         (y (- yyyy a))
         (m (- (+ mm (* 12 a)) 2)))
    (div (- (+ 7000 dd y (mod y 4) (mod y 400) (mod (* 31 m) 12)) (mod y 100)) 7)))
         