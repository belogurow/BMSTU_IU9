(define (sum f a n h i)
  (if (> i n)
      0          
      (+ (sum f a n h (+ i 1)) (f (+ a (* h i) (- (/ h 2))))))) 

(define (integrate f a b n)
  (let ((h (/ (- b a) n)))
    (* h (sum f a n h 0))))